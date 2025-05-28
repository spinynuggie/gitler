package org.example;

import java.util.*;
import java.util.stream.*;

public class GameMap {
    private static final Random RANDOM = new Random();
    private static final Map<Integer, List<Integer>> BUREN = new HashMap<>();
    static {
        // "buren" (easier math and no repeats)
        for (int i = 1; i <= 9; i++) {
            List<Integer> buur = new ArrayList<>();
            if (i % 3 != 1) buur.add(i - 1);
            if (i % 3 != 0) buur.add(i + 1);
            if (i > 3)       buur.add(i - 3);
            if (i <= 6)      buur.add(i + 3);
            BUREN.put(i, buur);
        }
    }


    final int roomCount;
    final List<Room> rooms;
    private final Map<Integer, Room> roomsById;

    public GameMap() {
        this.roomCount = RANDOM.nextInt(5) + 5;            // between 5 and 9 rooms
        List<Integer> ids = generateConnectedRoomIds(roomCount);
        int specialRoomId = pickSpecialRoomId(ids);

        this.rooms = new ArrayList<>(roomCount);
        this.roomsById = new HashMap<>();

        for (int id : ids) {
            boolean isSpecial = (id == specialRoomId);
            String opening = buildOpeningString(id, ids, isSpecial);
            Question q = Questions.get(RANDOM.nextInt(Questions.size()));
            EvaluationStrategy gemini = new GeminiEvaluationStrategy();
            Room room = Room.of(id, q.getName(), q.getText(), gemini, opening, isSpecial);
            rooms.add(room);
            roomsById.put(id, room);
        }
    }

    // Buurmannen aanmaken recoded, omdat het gewoon brute-forcte totdat het een goede combinatie vond. kan lang duren met veel processing als je ongeluk hebt
    private List<Integer> generateConnectedRoomIds(int count) {
        List<Integer> all = IntStream.rangeClosed(1, 9).boxed().toList();
        int seed = all.get(RANDOM.nextInt(all.size()));
        Set<Integer> connected = new LinkedHashSet<>();
        connected.add(seed);

        List<Integer> frontier = new ArrayList<>(BUREN.get(seed));
        while (connected.size() < count) {
            if (frontier.isEmpty()) {
                // zorgt ervoor bij de RARE BUG DAT 'frontiers' (buren met wie er 'connected' bestaan) leeg is, dat het toch de grid opnieuw checkt
                for (int id : new ArrayList<>(connected)) {
                    for (int n : BUREN.get(id)) {
                        if (!connected.contains(n)) frontier.add(n);
                    }
                }
            }
            int next = frontier.remove(RANDOM.nextInt(frontier.size()));
            if (connected.add(next)) {
                for (int n : BUREN.get(next)) {
                    if (!connected.contains(n) && !frontier.contains(n)) {
                        frontier.add(n);
                    }
                }
            }
        }
        return new ArrayList<>(connected);
    }

    private int pickSpecialRoomId(List<Integer> ids) {
        return ids.get(RANDOM.nextInt(ids.size()));
    }

    // optimized WASD om in ÉÉN methode te werken, en x is niet meer hardcoded.
    private String buildOpeningString(int id, List<Integer> ids, boolean isSpecial) {
        StringBuilder sb = new StringBuilder();
        if (isSpecial) sb.append("x");
        if (ids.contains(id - 3)) sb.append("W");
        if (ids.contains(id + 3)) sb.append("S");
        if (id % 3 != 1 && ids.contains(id - 1)) sb.append("A");
        if (id % 3 != 0 && ids.contains(id + 1)) sb.append("D");
        return sb.toString();
    }

    public Room getRoomById(int id) {
        return roomsById.get(id);
    }


    // buffers map for quicker loading times (useless but good for optimization)
    public void viewMap(Player player) {
        System.out.println("🗺️  Map");
        for (int row = 0; row < 3; row++) {
            StringBuilder[] lines = new StringBuilder[5];
            for (int i = 0; i < lines.length; i++) lines[i] = new StringBuilder();

            for (int col = 1; col <= 3; col++) {
                int id = row * 3 + col;
                Room r = roomsById.get(id);
                if (r != null) {
                    String o = r.opening;
                    lines[0].append(o.contains("W")
                            ? "+----  ----+ " : "+----------+ ");
                } else {
                    lines[0].append("             ");
                }
            }
            lines[0].append("\n");

            for (int col = 1; col <= 3; col++) {
                Room r = roomsById.get(row * 3 + col);
                if (r != null) {
                    String n = r.name;
                    lines[1].append(String.format(
                            "| %-9s| ",
                            n.length() > 9 ? n.substring(0,9) : n
                    ));
                } else {
                    lines[1].append("             ");
                }
            }
            lines[1].append("\n");

            for (int col = 1; col <= 3; col++) {
                int id = row * 3 + col;
                Room r = roomsById.get(id);
                if (r != null) {
                    boolean a = r.opening.contains("A"),
                            d = r.opening.contains("D");
                    if (a && d) {
                        lines[2].append(String.format("  kamer %-3d  ", id));
                    } else if (d) {
                        lines[2].append(String.format("| kamer %-3d  ", id));
                    } else if (a) {
                        lines[2].append(String.format("  kamer %-3d| ", id));
                    } else {
                        lines[2].append(String.format("| kamer %-3d| ", id));
                    }
                } else {
                    lines[2].append("     X       ");
                }
            }
            lines[2].append("\n");

            for (int col = 1; col <= 3; col++) {
                int id = row * 3 + col;
                if (player.currentRoom == id) {
                    lines[3].append("|    you   | ");
                } else if (roomsById.containsKey(id)) {
                    lines[3].append("|          | ");
                } else {
                    lines[3].append("             ");
                }
            }
            lines[3].append("\n");

            for (int col = 1; col <= 3; col++) {
                Room r = roomsById.get(row * 3 + col);
                if (r != null) {
                    lines[4].append(r.opening.contains("S")
                            ? "+----  ----+ " : "+----------+ ");
                } else {
                    lines[4].append("             ");
                }
            }
            lines[4].append("\n");

            // print the buffered stuff
            for (StringBuilder line : lines) {
                System.out.print(line);
            }
        }
    }
    public boolean kanBewegen(int fromId, int toId) {
        // 1) check buren
        List<Integer> opties = BUREN.getOrDefault(fromId, Collections.emptyList());
        if (!opties.contains(toId)) {
            return false;
        }
        // 2) check of kamer bestaat
        return roomsById.containsKey(toId);
    }
}
