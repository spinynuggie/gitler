package org.example;

import java.util.*;
import org.example.item.Key;

public class Game {
    public static void start(Scanner scanner, Player player) {
        GameMap gameMap = player.getMap();
        if (gameMap == null) {
            int gridSize = player.stage + 2; // stage 1: 3x3, stage 2: 4x4, etc.
            long seed = new Random().nextLong();
            gameMap = new GameMap(gridSize, seed);
            player.setMap(gameMap);
            player.currentRoom = gameMap.startRoomId;
        }
        Map<Integer, Monster> monstersPerRoom = new HashMap<>();

        while (true) {
            if (player.currentRoom == gameMap.exitId) {
                // Remove used keys from inventory
                int keysToRemove = gameMap.keysRequired;
                player.getInventory().removeItems(Key.class, keysToRemove);
                System.out.printf("%d sleutel(s) gebruikt om de deur te openen.\n", keysToRemove);

                player.stage++;
                if (player.stage > 3) {
                    System.out.println("Gefeliciteerd! Je hebt alle stages voltooid. Nu de eindbaas!");
                    finalBossBattle(scanner, player);
                    return;
                }
                System.out.println("Gefeliciteerd! Je gaat naar stage " + player.stage);
                int gridSize = player.stage + 2;
                long seed = new Random().nextLong();
                gameMap = new GameMap(gridSize, seed);
                player.setMap(gameMap);
                player.currentRoom = gameMap.startRoomId;
                player.completedRooms.clear();
                monstersPerRoom.clear();
                SaveManager.save(player);
            }

            System.out.println("Gebruik WASD om te bewegen (W=omhoog, A=links, S=omlaag, D=rechts), of 'exit' om te stoppen.");
            gameMap.viewMap(player);
            System.out.printf(Messages.HP_SCORE, player.hp, player.score);
            System.out.print(Messages.KEUZE_PROMPT);

            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println(Messages.TERUG_NAAR_MENU);
                SaveManager.save(player);
                return;
            }

            if (input.isEmpty()) {
                continue;
            }
            char move = input.charAt(0);

            int currentId = player.currentRoom;
            int gridSize = gameMap.getGridSize();
            int targetId = -1;

            switch (move) {
                case 'W': // Omhoog
                    if (currentId > gridSize) targetId = currentId - gridSize;
                    break;
                case 'A': // Links
                    if ((currentId - 1) % gridSize != 0) targetId = currentId - 1;
                    break;
                case 'S': // Omlaag
                    if (currentId <= (gridSize * gridSize) - gridSize) targetId = currentId + gridSize;
                    break;
                case 'D': // Rechts
                    if (currentId % gridSize != 0) targetId = currentId + 1;
                    break;
                default:
                    System.out.println(Messages.ONGELDIGE_INVOER);
                    continue;
            }

            if (targetId == -1) {
                System.out.println("Je kunt niet die kant op bewegen, daar is een muur.");
                continue;
            }

            // Controleer of de speler de uitgang probeert te bereiken
            if (targetId == gameMap.exitId) {
                long keyCount = player.getInventory().countItems(Key.class);
                if (keyCount >= gameMap.keysRequired) {
                    System.out.println("Je hebt de uitgang gevonden! Je gaat naar de volgende stage.");
                    SaveManager.save(player);
                    player.currentRoom = targetId; // Verplaats speler naar de uitgang voor de volgende iteratie
                    continue; // De lus zal de stage progressie afhandelen
                } else {
                    System.out.printf("De uitgang is op slot! Je hebt %d sleutel(s) nodig, maar je hebt er %d.\n", gameMap.keysRequired, keyCount);
                    continue; // Speler blijft in de huidige kamer
                }
            }

            // Speler verplaatst zich naar het nieuwe vakje
            player.currentRoom = targetId;

            // Controleer waar de speler is geland
            Room selectedRoom = gameMap.getRoomById(targetId);
            if (selectedRoom != null) {
                // De speler is een kamer binnengegaan, start de gevechtslogica
                if (player.completedRooms.contains(targetId)) {
                    System.out.println(Messages.KAMER_VOLTOOID);
                    continue;
                }

                Player.JokerResult jokerResult = player.vraagEnVerwerkJoker(scanner, "kamer");
                if (jokerResult == Player.JokerResult.GEBRUIKT) {
                    player.completedRooms.add(targetId);
                    player.score += 10;
                    player.tryAcquireSword(targetId, gameMap.getSwordRoomId());
                    if (selectedRoom.hasItem()) {
                        player.getInventory().addItem(selectedRoom.takeItem());
                        System.out.println("Je hebt een sleutel gevonden!");
                    }
                    SaveManager.save(player);
                    continue;
                } else if (jokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
                    continue;
                }

                Monster monster = monstersPerRoom.get(targetId);
                if (monster == null) {
                    monster = MonsterFactory.createMonsterFor();
                    monstersPerRoom.put(targetId, monster);
                }

                Battle battle = new Battle(scanner, player, monster, selectedRoom, gameMap);
                Battle.BattleResult result = battle.start();

                switch (result) {
                    case WIN:
                        System.out.printf(Messages.KAMER_SCORE, targetId);
                        player.currentRoom = targetId;
                        player.completedRooms.add(targetId);
                        player.score += 10;
                        player.tryAcquireSword(targetId, gameMap.getSwordRoomId());
                        if (selectedRoom.hasItem()) {
                            player.getInventory().addItem(selectedRoom.takeItem());
                            System.out.println("Je hebt een sleutel gevonden!");
                        }
                        SaveManager.save(player);
                        break;
                    case FLEE:
                        // Player stays in the previous room, no change in position.
                        System.out.println("Je blijft in de vorige ruimte.");
                        break;
                    case DEFEAT:
                        return; // Game over, return to main menu.
                }
            }
            // Als het geen kamer is, is het een 'X' vakje. Doe niets en laat de lus doorgaan.
        }
    }

    private static void finalBossBattle(Scanner scanner, Player player) {
        // Implement final boss battle logic here
        System.out.println("De eindbaas verschijnt!");
        Monster finalBoss = new Monster("Eindbaas", 5, 25, new AttackStrategy()); // 5 strength, 25 HP
        Battle battle = new Battle(scanner, player, finalBoss, null, player.getMap()); // Pass map for sword check
        battle.startFinalBossBattle();

        if (player.hp > 0) {
            System.out.println("Gefeliciteerd! Je hebt de eindbaas verslagen en het spel uitgespeeld!");
        } else {
            System.out.println("Helaas, de eindbaas was te sterk. Game over.");
        }
    }
}