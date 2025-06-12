package org.example;

import java.io.Serial;
import java.util.*;
import java.util.stream.*;
import org.example.questions.Question;
import java.io.Serializable;

public class GameMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int gridSize; // e.g., 3 for 3x3, 4 for 4x4
    private final Map<Integer, List<Integer>> buren;
    private static final List<String> CATEGORIES = new ArrayList<>(Questions.allCategories());

    private final Random rnd;
    public final int swordRoomId;
    final int roomCount;
    final List<Room> rooms;
    final Map<Integer, Room> roomsById;

    public GameMap() {
        this(3); // Default to 3x3
    }

    public GameMap(int gridSize) {
        this(gridSize, new Random().nextLong());
    }

    public GameMap(int gridSize, long seed) {
        this.gridSize = gridSize;
        this.rnd = new Random(seed);
        this.buren = generateBuren(gridSize);
        int maxRooms = gridSize * gridSize;
        this.roomCount = maxRooms - 1; // Always leave one X room
        List<Integer> ids = generateConnectedRoomIds(roomCount);
        this.swordRoomId = ids.get(rnd.nextInt(ids.size()));
        this.rooms = new ArrayList<>(roomCount);
        this.roomsById = new HashMap<>(roomCount);
        Set<Integer> idSet = new HashSet<>(ids);
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            String opening = buildOpeningString(id, idSet);
            String category = CATEGORIES.get(i % CATEGORIES.size());
            List<Question> vragen = new ArrayList<>(Questions.byCategory(category));
            Collections.shuffle(vragen, rnd);
            List<Question> vragenVoorKamer = vragen.subList(0, Math.min(5, vragen.size()));
            Question hoofdVraag = vragenVoorKamer.getFirst();
            Room room = Room.of(id, hoofdVraag.getCategory(), hoofdVraag.getText(), new GeminiEvaluationStrategy(), opening);
            rooms.add(room);
            roomsById.put(id, room);
        }
    }

    public int getSwordRoomId() {
        return swordRoomId;
    }

    public int getGridSize() {
        return gridSize;
    }

    private Map<Integer, List<Integer>> generateBuren(int gridSize) {
        Map<Integer, List<Integer>> tmp = new HashMap<>();
        int max = gridSize * gridSize;
        for (int i = 1; i <= max; i++) {
            List<Integer> buur = new ArrayList<>(4);
            if ((i - 1) % gridSize != 0) buur.add(i - 1); // West
            if (i % gridSize != 0) buur.add(i + 1); // East
            if (i > gridSize) buur.add(i - gridSize); // North
            if (i <= max - gridSize) buur.add(i + gridSize); // South
            tmp.put(i, Collections.unmodifiableList(buur));
        }
        return Collections.unmodifiableMap(tmp);
    }

    private List<Integer> generateConnectedRoomIds(int count) {
        int max = gridSize * gridSize;
        List<Integer> all = IntStream.rangeClosed(1, max).boxed().toList();
        int start = all.get(rnd.nextInt(all.size()));
        Set<Integer> connected = new LinkedHashSet<>();
        connected.add(start);
        LinkedList<Integer> frontier = new LinkedList<>(buren.get(start));
        while (connected.size() < count) {
            if (frontier.isEmpty()) {
                for (int id : new ArrayList<>(connected))
                    for (int n : buren.get(id))
                        if (!connected.contains(n)) frontier.add(n);
            }
            int next = frontier.remove(rnd.nextInt(frontier.size()));
            if (connected.add(next))
                for (int n : buren.get(next))
                    if (!connected.contains(n) && !frontier.contains(n)) frontier.add(n);
        }
        return new ArrayList<>(connected);
    }

    private String buildOpeningString(int id, Set<Integer> idSet) {
        StringBuilder sb = new StringBuilder(4);
        if (idSet.contains(id - gridSize)) sb.append('N');
        if (idSet.contains(id + gridSize)) sb.append('Z');
        if ((id - 1) % gridSize != 0 && idSet.contains(id - 1)) sb.append('W');
        if (id % gridSize != 0 && idSet.contains(id + 1)) sb.append('O');
        return sb.toString();
    }

    public Room getRoomById(int id) {
        return roomsById.get(id);
    }

    public void viewMap(Player player) {
        System.out.println(Messages.MAP_HEADER);
        for (int row = 0; row < gridSize; row++) {
            StringBuilder[] lines = new StringBuilder[5];
            for (int i = 0; i < 5; i++) {
                lines[i] = new StringBuilder();
            }
            for (int col = 1; col <= gridSize; col++) {
                int id = row * gridSize + col;
                Room r = roomsById.get(id);
                String opening = (r != null ? r.opening : "");
                if (r != null && opening.contains("N")) lines[0].append("+----  ----+ ");
                else if (r != null)                  lines[0].append("+----------+ ");
                else                                 lines[0].append("             ");
            }
            lines[0].append('\n');

            for (int col = 1; col <= gridSize; col++) {
                Room r = roomsById.get(row * gridSize + col);
                if (r != null) {
                    String name = r.name.length() > 9 ? r.name.substring(0, 9) : r.name;
                    lines[1].append(String.format("| %-9s| ", name));
                } else {
                    lines[1].append("             ");
                }
            }
            lines[1].append('\n');

            for (int col = 1; col <= gridSize; col++) {
                int id = row * gridSize + col;
                Room r = roomsById.get(id);
                if (r != null) {
                    String opening = r.opening;
                    boolean west  = opening.contains("W");
                    boolean oost = opening.contains("O");
                    if (west && oost)      lines[2].append(String.format("  kamer %-3d  ", id));
                    else if (oost)         lines[2].append(String.format("| kamer %-3d  ", id));
                    else if (west)         lines[2].append(String.format("  kamer %-3d| ", id));
                    else                   lines[2].append(String.format("| kamer %-3d| ", id));
                } else {
                    lines[2].append("     X       ");
                }
            }
            lines[2].append('\n');

            for (int col = 1; col <= gridSize; col++) {
                int id = row * gridSize + col;
                if (player.currentRoom == id)         lines[3].append("|    you   | ");
                else if (roomsById.containsKey(id))    lines[3].append("|          | ");
                else                                   lines[3].append("             ");
            }
            lines[3].append('\n');

            for (int col = 1; col <= gridSize; col++) {
                int id = row * gridSize + col;
                Room r = roomsById.get(id);
                String opening = (r != null ? r.opening : "");
                if (r != null && opening.contains("Z")) lines[4].append("+----  ----+ ");
                else if (r != null)                    lines[4].append("+----------+ ");
                else                                   lines[4].append("             ");
            }
            lines[4].append('\n');

            for (StringBuilder sb : lines) {
                System.out.print(sb);
            }
        }
        // Show inventory after the map
        System.out.println(Messages.INVENTORY_HEADER);
        var items = player.getInventory().getAllItems();
        if (items.isEmpty()) {
            System.out.println(Messages.INVENTORY_EMPTY);
        } else {
            for (var item : items) {
                System.out.printf(Messages.INVENTORY_ITEM + "%n", item.getName(), item.getDescription());
            }
        }
    }

    public boolean kanBewegen(int fromId, int toId) {
        return buren.getOrDefault(fromId, Collections.emptyList()).contains(toId)
                && roomsById.containsKey(toId);
    }
}
