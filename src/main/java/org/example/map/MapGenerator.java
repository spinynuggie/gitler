package org.example.map;

import org.example.Room;
import org.example.questions.Question;
import org.example.Questions;
import org.example.GeminiEvaluationStrategy;

import java.util.*;
import java.util.stream.IntStream;

public class MapGenerator {

    private final int gridSize;
    private final Random rnd;
    private final Map<Integer, List<Integer>> buren;

    public MapGenerator(int gridSize, long seed) {
        this.gridSize = gridSize;
        this.rnd = new Random(seed);
        this.buren = generateBuren(gridSize);
    }

    public MapData generate() {
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
        int roomCount = rnd.nextInt(maxRoomsForGrid - minRooms + 1) + minRooms;
        int keysRequired = switch (gridSize) {
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

        int startRoomId = xRoomIds.get(rnd.nextInt(xRoomIds.size()));

        List<Integer> potentialExitIds = new ArrayList<>(xRoomIds);
        potentialExitIds.remove(Integer.valueOf(startRoomId));

        int exitId = findFarthestEdgeXRoom(startRoomId, potentialExitIds);
        int swordRoomId = roomIds.get(rnd.nextInt(roomIds.size()));

        List<Room> rooms = new ArrayList<>(roomCount);
        Map<Integer, Room> roomsById = new HashMap<>(roomCount);
        Set<Integer> idSet = new HashSet<>(roomIds);
        List<String> CATEGORIES = new ArrayList<>(Questions.allCategories());
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

        return new MapData(gridSize, buren, swordRoomId, exitId, startRoomId, roomCount, rooms, roomsById, keysRequired);
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

    private int findFarthestEdgeXRoom(int startId, List<Integer> xRooms) {
        if (xRooms.isEmpty()) {
            return -1;
        }

        int startX = (startId - 1) % gridSize;
        int startY = (startId - 1) / gridSize;

        int farthestRoomId = -1;
        double maxDist = -1;

        for (int xRoomId : xRooms) {
            int currentX = (xRoomId - 1) % gridSize;
            int currentY = (xRoomId - 1) / gridSize;

            if (currentX == 0 || currentX == gridSize - 1 || currentY == 0 || currentY == gridSize - 1) {
                double dist = Math.sqrt(Math.pow(startX - currentX, 2) + Math.pow(startY - currentY, 2));
                if (dist > maxDist) {
                    maxDist = dist;
                    farthestRoomId = xRoomId;
                }
            }
        }

        return farthestRoomId == -1 ? xRooms.get(rnd.nextInt(xRooms.size())) : farthestRoomId;
    }
} 