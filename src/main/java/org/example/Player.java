package org.example;

import org.example.item.Inventory;
import org.example.item.Joker;
import org.example.item.Sword;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Player implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    public int currentRoom;
    public int score;
    public int hp;
    public Set<Integer> completedRooms = new HashSet<>();
    private GameMap map;
    private final Inventory inventory;

    public Player(int startHp) {
        this.inventory = new Inventory();
        this.inventory.addItem(new Joker()); // Start with a joker
        this.hp = startHp;
    }

    public void setMap(GameMap map) {
        this.map = map;
        // Zoek een X-kamer (id zonder Room)
        int xRoomId = -1;
        for (int id = 1; id <= map.getGridSize() * map.getGridSize(); id++) {
            if (!map.roomsById.containsKey(id)) {
                xRoomId = id;
                break;
            }
        }
        // Als er geen X-kamer is, fallback naar kamer 1
        if (xRoomId == -1) {
            xRoomId = 1;
        }
        this.currentRoom = xRoomId;
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
}
