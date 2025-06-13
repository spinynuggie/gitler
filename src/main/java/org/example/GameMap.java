package org.example;

import org.example.item.Key;
import org.example.map.MapData;
import org.example.map.MapGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class GameMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public final int gridSize;
    private final Map<Integer, List<Integer>> buren;

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
        MapGenerator generator = new MapGenerator(gridSize, seed);
        MapData data = generator.generate();

        this.gridSize = data.gridSize();
        this.buren = data.buren();
        this.swordRoomId = data.swordRoomId();
        this.exitId = data.exitId();
        this.startRoomId = data.startRoomId();
        this.roomCount = data.roomCount();
        this.rooms = data.rooms();
        this.roomsById = data.roomsById();
        this.keysRequired = data.keysRequired();

        placeKeys(keysRequired, new Random(seed));
    }

    private void placeKeys(int numKeys, Random rnd) {
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

    public Room getRoomById(int id) {
        return roomsById.get(id);
    }
}
