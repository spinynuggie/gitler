package org.example;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    public int currentRoom;
    public int score;
    public int hp;
    public Set<Integer> completedRooms = new HashSet<>();

    public Player(int startRoom, int startHp) {
        this.currentRoom = startRoom;
        this.hp = startHp;
    }
}
