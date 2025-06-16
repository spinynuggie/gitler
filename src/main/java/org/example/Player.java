package org.example;

import com.google.gson.internal.bind.util.ISO8601Utils;
import org.example.item.Inventory;
import org.example.item.Joker;
import org.example.item.Sword;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Player implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    public int currentRoom;
    public int score;
    private int hp;
    public int stage;
    public Set<Integer> completedRooms = new HashSet<>();
    private GameMap map;
    private final Inventory inventory;
    private transient List<PlayerObserver> observers = new ArrayList<>();

    public Player(int startHp) {
        this.inventory = new Inventory();
        this.inventory.addItem(new Joker()); // Start with a joker
        this.hp = startHp;
        this.stage = 1;
    }

    public void setMap(GameMap map) {
        this.map = map;
        // The player's currentRoom is now set in Game.java before map creation
        // to ensure the exit is placed correctly relative to the player's start.
    }

    public GameMap getMap() {
        return map;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean hasJoker() {
        return inventory.getItem(Joker.class)
                .map(Joker::isAvailable)
                .orElse(false);
    }

    public void useJoker() {
        inventory.getItem(Joker.class).ifPresent(joker -> {
            joker.use();
            inventory.removeItem(joker);
        });
    }

    public int getAttackDamage() {
        return inventory.getItem(Sword.class)
                .map(Sword::getDamage)
                .orElse(1); // Default damage without sword
    }

    public enum JokerResult {
        NIET_GEBRUIKT,
        GEEN_JOKER_MEER,
        GEBRUIKT
    }

    /**
     * Vraagt de speler of hij een joker wil gebruiken en verwerkt dit.
     * @param scanner Scanner voor gebruikersinvoer
     * @param context String ("kamer" of "vraag") voor de prompt
     * @return JokerResult status
     */
    public JokerResult vraagEnVerwerkJoker(Scanner scanner, String context) {
        if (!hasJoker()) {
            // Geen joker? Sla de vraag over, ga gewoon verder zonder joker te gebruiken!
            return JokerResult.NIET_GEBRUIKT;
        }
        System.out.printf(Messages.JOKER_PROMPT, context);
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("j") || input.equalsIgnoreCase("y")) {
            useJoker();
            System.out.printf((Messages.JOKER_GEBRUIKT) + "%n", context.substring(0, 1).toUpperCase() + context.substring(1));
            return JokerResult.GEBRUIKT;
        }
        return JokerResult.NIET_GEBRUIKT;
    }

    /**
     * Probeert het zwaard toe te voegen als de speler in de juiste kamer is en het nog niet heeft.
     * @param roomId huidige kamer
     * @param swordRoomId id van de zwaardkamer
     */
    public void tryAcquireSword(int roomId, int swordRoomId) {
        if (roomId == swordRoomId && !getInventory().hasItem(Sword.class)) {
            getInventory().addItem(new Sword());
            System.out.println(Messages.ZWAARD_GEVONDEN);
        }
    }

    public void addObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PlayerObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (PlayerObserver observer : observers) {
            observer.update(this);
        }
    }

    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - amount);
        notifyObservers();
    }

    public void setHp(int hp) {
        this.hp = hp;
        notifyObservers();
    }

    public int getHp() {
        return hp;
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        observers = new ArrayList<>();
    }
}
