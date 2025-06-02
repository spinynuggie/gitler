package org.example;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Player implements Serializable {

    @Serial private static final long serialVersionUID = 1L;
    public boolean jokerAvailable = true;
    public int currentRoom;
    public int score;
    public int hp;
    public Set<Integer> completedRooms = new HashSet<>();
    private long mapSeed;
    private transient GameMap map;

    public Player(int startRoom, int startHp) {
        this.currentRoom = startRoom;
        this.hp = startHp;
        this.map = new GameMap();
        this.mapSeed = map.getSeed();
    }

    public GameMap getMap() {
        if (map == null) {
            map = new GameMap(mapSeed);
        }
        return map;
    }
}
