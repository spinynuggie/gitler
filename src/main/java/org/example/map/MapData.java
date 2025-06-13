package org.example.map;

import org.example.Room;

import java.util.List;
import java.util.Map;

public record MapData(
    int gridSize,
    Map<Integer, List<Integer>> buren,
    int swordRoomId,
    int exitId,
    int startRoomId,
    int roomCount,
    List<Room> rooms,
    Map<Integer, Room> roomsById,
    int keysRequired
) {} 