package org.example.map;

import org.example.GameMap;
import org.example.item.Key;
import org.example.Messages;
import org.example.Player;
import org.example.Room;

public class MapConsoleRenderer {

    public void viewMap(GameMap gameMap, Player player) {
        System.out.println(Messages.MAP_HEADER);
        for (int row = 0; row < gameMap.getGridSize(); row++) {
            StringBuilder[] lines = new StringBuilder[5];
            for (int i = 0; i < 5; i++) {
                lines[i] = new StringBuilder();
            }
            for (int col = 1; col <= gameMap.getGridSize(); col++) {
                int id = row * gameMap.getGridSize() + col;
                Room r = gameMap.getRoomById(id);
                String opening = (r != null ? r.opening : "");
                if (r != null && opening.contains("N")) lines[0].append("+----  ----+ ");
                else if (r != null)                  lines[0].append("+----------+ ");
                else                                 lines[0].append("             ");
            }
            lines[0].append('\n');

            for (int col = 1; col <= gameMap.getGridSize(); col++) {
                Room r = gameMap.getRoomById(row * gameMap.getGridSize() + col);
                if (r != null) {
                    String name = r.name.length() > 9 ? r.name.substring(0, 9) : r.name;
                    lines[1].append(String.format("| %-9s| ", name));
                } else {
                    lines[1].append("             ");
                }
            }
            lines[1].append('\n');

            for (int col = 1; col <= gameMap.getGridSize(); col++) {
                int id = row * gameMap.getGridSize() + col;
                Room r = gameMap.getRoomById(id);
                if (r != null) {
                    String opening = r.opening;
                    boolean west = opening.contains("W");
                    boolean oost = opening.contains("O");
                    String left = west ? " " : "|";
                    String right = oost ? " " : "|";
                    lines[2].append(String.format("%s  Kamer   %s ", left, right));
                } else if (id == gameMap.exitId) {
                    lines[2].append("    EXIT     ");
                } else {
                    lines[2].append("     X       ");
                }
            }
            lines[2].append('\n');

            for (int col = 1; col <= gameMap.getGridSize(); col++) {
                int id = row * gameMap.getGridSize() + col;
                if (player.currentRoom == id)         lines[3].append("|    \uD83E\uDDD1    | ");
                else if (id == gameMap.exitId) {
                    long keyCount = player.getInventory().countItems(Key.class);
                    if (keyCount >= gameMap.keysRequired) {
                        lines[3].append("|   EXIT   | ");
                    } else {
                        lines[3].append(String.format("| \uD83D\uDD12 %d/%d   | ", keyCount, gameMap.keysRequired));
                    }
                }
                else if (gameMap.getRoomById(id) != null)    lines[3].append("|          | ");
                else                                   lines[3].append("             ");
            }
            lines[3].append('\n');

            for (int col = 1; col <= gameMap.getGridSize(); col++) {
                int id = row * gameMap.getGridSize() + col;
                Room r = gameMap.getRoomById(id);
                String opening = (r != null ? r.opening : "");
                if (r != null && opening.contains("Z")) lines[4].append("+----  ----+ ");
                else if (r != null)                    lines[4].append("+----------+ ");
                else                                   lines[4].append("             ");
            }
            lines[4].append('\n');

            for (StringBuilder sb : lines) {
                System.out.print(sb);
            }
        }
        // Show inventory after the map
        System.out.println(Messages.INVENTORY_HEADER);
        var items = player.getInventory().getAllItems();
        if (items.isEmpty()) {
            System.out.println(Messages.INVENTORY_EMPTY);
        } else {
            for (var item : items) {
                System.out.printf(Messages.INVENTORY_ITEM + "%n", item.getName(), item.getDescription());
            }
        }
    }
} 