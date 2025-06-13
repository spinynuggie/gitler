package org.example;

import java.io.Serial;
import java.util.*;
import java.util.stream.*;
import org.example.questions.Question;
import org.example.item.Key;
import java.io.Serializable;

public class GameMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int gridSize; // e.g., 3 for 3x3, 4 for 4x4
    private final Map<Integer, List<Integer>> buren;
    private static final List<String> CATEGORIES = new ArrayList<>(Questions.allCategories());

    private final Random rnd;
    public final int swordRoomId;
    public final int exitId;
    public final int startRoomId;
    final int roomCount;
    final List<Room> rooms;
    final Map<Integer, Room> roomsById;
    public final int keysRequired;

    public GameMap() {
        this(3, new Random().nextLong());
    }

    public GameMap(int gridSize) {
        this(gridSize, new Random().nextLong());
    }

    public GameMap(int gridSize, long seed) {
        this.gridSize = gridSize;
        this.rnd = new Random(seed);
        this.buren = generateBuren(gridSize);
        int maxGridId = gridSize * gridSize;

        int minRooms, maxRoomsForGrid;
        switch (gridSize) {
            case 3: minRooms = 5; maxRoomsForGrid = 7; break;
            case 4: minRooms = 10; maxRoomsForGrid = 14; break;
            case 5: minRooms = 17; maxRoomsForGrid = 23; break;
            default:
                minRooms = (int) (maxGridId * 0.5);
                maxRoomsForGrid = (int) (maxGridId * 0.9);
                break;
        }
        this.roomCount = rnd.nextInt(maxRoomsForGrid - minRooms + 1) + minRooms;
        this.keysRequired = switch (gridSize) {
            case 3 -> 1;
            case 4 -> 3;
            case 5 -> 5;
            default -> 1;
        };

        List<Integer> roomIds = generateConnectedRoomIds(roomCount);
        Set<Integer> roomIdsSet = new HashSet<>(roomIds);
        List<Integer> xRoomIds = new ArrayList<>();
        for (int i = 1; i <= maxGridId; i++) {
            if (!roomIdsSet.contains(i)) {
                xRoomIds.add(i);
            }
        }

        this.startRoomId = xRoomIds.get(rnd.nextInt(xRoomIds.size()));

        List<Integer> potentialExitIds = new ArrayList<>(xRoomIds);
        potentialExitIds.remove(Integer.valueOf(startRoomId));

        this.exitId = findFarthestEdgeXRoom(startRoomId, potentialExitIds);
        this.swordRoomId = roomIds.get(rnd.nextInt(roomIds.size()));
        
        this.rooms = new ArrayList<>(roomCount);
        this.roomsById = new HashMap<>(roomCount);
        Set<Integer> idSet = new HashSet<>(roomIds);
        for (int i = 0; i < roomIds.size(); i++) {
            int id = roomIds.get(i);
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
        placeKeys(keysRequired);
    }

    private void placeKeys(int numKeys) {
        List<Room> possibleRooms = new ArrayList<>(rooms);
        // Ensure sword room doesn't have a key
        possibleRooms.remove(roomsById.get(swordRoomId));
        Collections.shuffle(possibleRooms, rnd);

        for (int i = 0; i < numKeys && i < possibleRooms.size(); i++) {
            possibleRooms.get(i).setItem(new Key());
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
                    boolean west = opening.contains("W");
                    boolean oost = opening.contains("O");
                    String left = west ? " " : "|";
                    String right = oost ? " " : "|";
                    lines[2].append(String.format("%s  Kamer   %s ", left, right));
                } else if (id == exitId) {
                    lines[2].append("    EXIT     ");
                } else {
                    lines[2].append("     X       ");
                }
            }
            lines[2].append('\n');

            for (int col = 1; col <= gridSize; col++) {
                int id = row * gridSize + col;
                if (player.currentRoom == id)         lines[3].append("|    \uD83E\uDDD1    | ");
                else if (id == exitId) {
                    long keyCount = player.getInventory().countItems(Key.class);
                    if (keyCount >= keysRequired) {
                        lines[3].append("|   EXIT   | ");
                    } else {
                        lines[3].append(String.format("| \uD83D\uDD12 %d/%d   | ", keyCount, keysRequired));
                    }
                }
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

    public boolean allRoomsCompleted(Set<Integer> completedRooms) {
        return completedRooms.equals(roomsById.keySet());
    }

    private int findFarthestEdgeXRoom(int startId, List<Integer> xRooms) {
        List<Integer> edgeXRooms = new ArrayList<>();
        for (int id : xRooms) {
            int row = (id - 1) / gridSize;
            int col = (id - 1) % gridSize;
            if (row == 0 || row == gridSize - 1 || col == 0 || col == gridSize - 1) {
                edgeXRooms.add(id);
            }
        }

        // Verwijder buren van de startpositie om te voorkomen dat je naast de uitgang spawnt
        List<Integer> startNeighbors = buren.getOrDefault(startId, Collections.emptyList());
        List<Integer> nonAdjacentEdgeXRooms = new ArrayList<>(edgeXRooms);
        nonAdjacentEdgeXRooms.removeAll(startNeighbors);

        // Gebruik de niet-aangrenzende kamers indien mogelijk, anders de volledige lijst met randkamers
        List<Integer> searchList = nonAdjacentEdgeXRooms.isEmpty() ? edgeXRooms : nonAdjacentEdgeXRooms;

        if (searchList.isEmpty()) {
            // Fallback voor het geval er geen rand-X-kamers zijn (onwaarschijnlijk)
            if (xRooms.isEmpty()) {
                return -1; // Mag niet gebeuren
            }
            // Kies een willekeurige X-kamer die niet de startkamer is
            return xRooms.get(rnd.nextInt(xRooms.size()));
        }

        int farthestRoom = -1;
        int maxDist = -1;

        int startX = (startId - 1) % gridSize;
        int startY = (startId - 1) / gridSize;

        for (int edgeRoomId : searchList) {
            int edgeX = (edgeRoomId - 1) % gridSize;
            int edgeY = (edgeRoomId - 1) / gridSize;
            int dist = Math.abs(startX - edgeX) + Math.abs(startY - edgeY); // Manhattan-afstand
            if (dist > maxDist) {
                maxDist = dist;
                farthestRoom = edgeRoomId;
            }
        }
        return farthestRoom;
    }

    public boolean isAdjacent(int fromId, int toId) {
        return buren.getOrDefault(fromId, Collections.emptyList()).contains(toId);
    }
}
