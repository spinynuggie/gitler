package org.example;

import java.util.*;

public class Game {
    public static void start(Scanner scanner, Player player) {
        GameMap gameMap = player.getMap();
        Map<Integer, Monster> monstersPerRoom = new HashMap<>();

        while (true) {
            System.out.println("\n🎯 Kies een kamer (of typ 'exit'):");
            gameMap.viewMap(player);
            System.out.printf("%n📍 HP: %d | Score: %d%n", player.hp, player.score);
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

            if (player.completedRooms.contains(roomId)) {
                player.currentRoom = roomId;
                System.out.println("✅ Deze kamer heb je al voltooid.");
                continue;
            }

            if (!gameMap.kanBewegen(player.currentRoom, roomId)) {
                System.out.println("❌ Je kunt niet naar kamer " + roomId + " lopen — niet aangrenzend!");
                continue;
            }

            System.out.print("Wil je een joker gebruiken om de kamer over te slaan? Y/N\n");
            String jokerInput = scanner.nextLine().trim();
            if (jokerInput.equalsIgnoreCase("Y")) {
                if (!player.jokerAvailable) {
                    System.out.println("❌ Je hebt je joker al gebruikt!");
                } else {
                    player.jokerAvailable = false;
                    player.currentRoom = roomId;
                    player.completedRooms.add(roomId);
                    player.score += 10;
                    if (roomId == gameMap.getSwordRoomId() && !player.inventory.contains("Sword")) {
                        player.inventory.add("Sword");
                        System.out.println("🗡️ Je vond een ZWAARD in deze kamer!");
                    }
                    System.out.println("🃏 Joker gebruikt! Kamer automatisch voltooid.");
                    SaveManager.save(player);
                }
                continue;
            }

            Monster monster = monstersPerRoom.get(roomId);
            if (monster == null) {
                monster = MonsterFactory.createMonsterFor(selectedRoom);
                monstersPerRoom.put(roomId, monster);
            }

            while (monster.isAlive()) {
                boolean correct = selectedRoom.play(scanner, player);
                if (correct) {
                    int damage = player.inventory.contains("Sword") ? 3 : 1;
                    monster.takeDamage(damage);

                    if (!monster.isAlive()) {
                        System.out.println("🏆 Je hebt het monster verslagen!");
                        if (roomId == gameMap.getSwordRoomId() && !player.inventory.contains("Sword")) {
                            player.inventory.add("Sword");
                            System.out.println("🗡️ Je vond een ZWAARD in deze kamer!");
                        }
                        player.currentRoom = roomId;
                        player.score += 10;
                        player.completedRooms.add(roomId);
                        System.out.printf("✅ Goed! Kamer %d voltooid. +10 score%n", roomId);
                        SaveManager.save(player);
                        break;
                    } else {
                        System.out.println("⚔️ Het monster leeft nog! Je moet nog een vraag beantwoorden.");
                    }

                } else {
                    monster.hinder(player);
                    String vraag = selectedRoom.getVraag();
                    if (vraag != null) {
                        HintSystem.maybeGiveHint(scanner, vraag);
                    }
                    SaveManager.save(player);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    if (player.hp <= 0) {
                        System.out.println("\n💀 Je hebt geen HP meer. Game over!");
                        SaveManager.save(player);
                        return;
                    }
                    break;
                }
            }
        }
    }
}