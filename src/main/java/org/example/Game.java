package org.example;

import java.util.*;

public class Game {
    public static void start(Scanner scanner, Player player) {

        Monster monster = new Monster(
                "Goblin",           // name
                3,                  // strength
                10,                 // monster health
                new AttackStrategy()
        );

        // --- map+room setup (uses your updated Room signature internally) ---
        GameMap gameMap = new GameMap();

        // --- main game loop ---
        while (true) {
            System.out.println("\n🎯 Kies een kamer (of typ 'exit'):");
            gameMap.viewMap(player);                                  // show the map
            System.out.printf("%n📍 HP: %d | Score: %d%n",             // stats
                    player.hp, player.score);

            System.out.print("Keuze: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("↩️  Terug naar hoofdmenu...");
                SaveManager.save(player);
                return;
            }

            int roomId;
            try {
                roomId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Ongeldige invoer. Probeer opnieuw.");
                continue;
            }

            // lookup only in GameMap’s room list
            Room selectedRoom = null;
            for (Room r : gameMap.kamers) {
                if (r.id == roomId) {
                    selectedRoom = r;
                    break;
                }
            }
            if (selectedRoom == null) {
                System.out.println("⚠️ Kamer " + roomId + " niet toegankelijk. Probeer opnieuw.");
                continue;
            }

            // play the room’s challenge
            boolean correct = selectedRoom.play(scanner);

            if (correct) {
                // success handling
                player.currentRoom = roomId;
                player.score += 10;
                if (!player.completedRooms.contains(roomId)) {
                    player.completedRooms.add(roomId);
                }
                System.out.println("✅ Goed! +10 score");
                System.out.printf("Voortgang: kamer %d | Score: %d | HP: %d%n",
                        player.currentRoom, player.score, player.hp);
                SaveManager.save(player);

            } else {
                // failure handling
                monster.hinder(player);

                // contextual hint based on room id
                Room currentRoom = gameMap.getRoomById(roomId);
                String vraag = (currentRoom != null)
                        ? currentRoom.getVraag()
                        : "Geen vraag gevonden.";
                if (vraag != null) {
                    HintSystem.maybeGiveHint(scanner, vraag);
                }
                SaveManager.save(player);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // game-over check
                if (player.hp <= 0) {
                    System.out.println("\n💀 Je hebt geen HP meer. Game over!");
                    SaveManager.save(player);
                    return;
                }
            }
        }
    }
}
