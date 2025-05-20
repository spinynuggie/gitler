package org.example;

import java.util.*;

public class Game {
    public static void start(Scanner scanner, Player player) {
        GameMap map = new GameMap();
        Random rng = new Random();
        int pos = player.currentRoom > 0 ? player.currentRoom : 7; // choose a sensible start

        while (true) {
            System.out.println("\n🗺️  Jouw map:");
            map.viewMap(pos);
            System.out.printf("📍 Positie: %d | HP: %d | Score: %d%n",
                    pos, player.hp, player.score);

            System.out.print("Move w/a/s/d of typ 'exit': ");
            String in = scanner.nextLine().trim().toLowerCase();
            if (in.equals("exit")) return;

            int row = (pos-1)/5, col = (pos-1)%5;
            switch (in) {
                case "w": row = Math.max(0, row-1); break;
                case "s": row = Math.min(4, row+1); break;
                case "a": col = Math.max(0, col-1); break;
                case "d": col = Math.min(4, col+1); break;
                default:
                    System.out.println("⚠️ Ongeldige richting!");
                    continue;
            }
            pos = row*5 + col + 1;

            Room room = map.getRoom(pos);
            if (room == null) {
                System.out.println("🌳 Je loopt door een lege gang. Niets gebeurt.");
                continue;
            }

            // 50% chance op encounter
            if (rng.nextDouble() < 0.5) {
                boolean win = room.play(scanner);
                if (win) {
                    if (!player.completedRooms.contains(pos)) {
                        player.score += 10;
                        player.completedRooms.add(pos);
                    }
                    System.out.println("🏆 Monster verslagen! +10 score");
                } else {
                    player.hp--;
                    System.out.println("💥 Je bent geraakt! -1 HP");
                    if (player.hp <= 0) {
                        System.out.println("💀 Je bent dood. Game Over!");
                        return;
                    }
                }
                player.currentRoom = pos;
                SaveManager.save(player);
            } else {
                System.out.println("😌 Geen gevaar hier, je loopt door...");
            }
        }
    }
}