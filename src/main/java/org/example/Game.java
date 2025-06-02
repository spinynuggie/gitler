package org.example;

import java.util.*;

public class Game {
    public static void start(Scanner scanner, Player player) {

        Monster monster = new Monster(
                "Goblin",           // name
                3,                  // strength
                10,                 // health
                new AttackStrategy()
        );

        // ← Reuse the same seeded map from Player
        GameMap gameMap = player.getMap();

        while (true) {
            System.out.println("\n🎯 Kies een kamer (of typ 'exit'):");
            gameMap.viewMap(player);
            System.out.printf("%n📍 HP: %d | Score: %d%n",
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

            Room selectedRoom = gameMap.getRoomById(roomId);
            if (selectedRoom == null) {
                System.out.println("⚠️ Kamer " + roomId + " bestaat niet. Probeer opnieuw.");
                continue;
            }

            if (!gameMap.kanBewegen(player.currentRoom, roomId)) {
                System.out.println("❌ Je kunt niet naar kamer " + roomId + " lopen — niet aangrenzend!");
                continue;
            }

            // ─── Vervang de oude joker‐prompt door één regel waarin we de 'play' methode met joker‐logica aanroepen ───
            boolean correct = selectedRoom.play(scanner, player);
            // ─────────────────────────────────────────────────────────────────────────

            if (correct) {
                player.currentRoom = roomId;
                player.score += 10;
                player.completedRooms.add(roomId);
                System.out.println("✅ Goed! +10 score");
                System.out.printf("Voortgang: kamer %d | Score: %d | HP: %d%n",
                        player.currentRoom, player.score, player.hp);
                SaveManager.save(player);

            } else {
                monster.hinder(player);

                String vraag = selectedRoom.getVraag();
                if (vraag != null) {
                    HintSystem.maybeGiveHint(scanner, vraag);
                }
                SaveManager.save(player);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (player.hp <= 0) {
                    System.out.println("\n💀 Je hebt geen HP meer. Game over!");
                    SaveManager.save(player);
                    return;
                }
            }
        }
    }
}
