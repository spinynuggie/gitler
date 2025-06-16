package org.example.map;

import org.example.GameMap;
import org.example.item.Key;
import org.example.Messages;
import org.example.Player;
import org.example.Room;

import java.util.ArrayList;
import java.util.List;

public class MapConsoleRenderer {
    // fixed cell width (including the trailing space)
    private static final String CELL_FORMAT = "| %-9s| ";

    public void viewMap(GameMap gameMap, Player player) {
        System.out.println(Messages.MAP_HEADER);
        int size = gameMap.getGridSize();

        for (int row = 0; row < size; row++) {
            // build 5 lines for this row of cells
            List<StringBuilder> lines = new ArrayList<>();
            for (int i = 0; i < 5; i++) lines.add(new StringBuilder());

            for (int col = 1; col <= size; col++) {
                int id            = row * size + col;
                Room room         = gameMap.getRoomById(id);
                boolean isExit       = id == gameMap.exitId;
                boolean isPlayerHere = player.currentRoom == id;

                // Use room's actual openings if room exists, otherwise use default
                String opening = room != null ? room.opening : defaultOpening(row, col, size);

                // 1) Top border
                lines.get(0).append(
                        opening.contains("N")
                                ? "+----  ----+ "
                                : "+----------+ "
                );

                // 2) Room name (or blank)
                String nameLabel = room != null
                        ? shorten(room.name, 9)
                        : "";
                String leftBorder = opening.contains("W") ? " " : "|";
                String rightBorder = opening.contains("O") ? " " : "|";
                lines.get(1).append(String.format("| %-9s| ", nameLabel));

                // 3) "Kamer" / "EXIT" / "X"
                String middleLabel;
                if (isExit) {
                    middleLabel = center("EXIT", 9);
                } else if (room != null) {
                    middleLabel = "Kamer";   // will be left-padded by format
                } else {
                    middleLabel = center("X", 9);
                }
                lines.get(2).append(String.format("%s %-9s%s ", leftBorder, middleLabel, rightBorder));

                // 4) Player / lock or empty
                String statusLabel;
                if (isPlayerHere) {
                    statusLabel = center("🧑", 9);
                } else if (isExit) {
                    long keys = player.getInventory().countItems(Key.class);
                    if (keys >= gameMap.keysRequired) {
                        statusLabel = center("EXIT", 9);
                    } else {
                        // e.g. "🔒 2/3"
                        statusLabel = center("🔒 " + keys + "/" + gameMap.keysRequired, 9);
                    }
                } else {
                    statusLabel = "";
                }
                lines.get(3).append(String.format("| %-9s| ", statusLabel));

                // 5) Bottom border
                lines.get(4).append(
                        opening.contains("Z")
                                ? "+----  ----+ "
                                : "+----------+ "
                );
            }

            // now print those 5 perfectly aligned lines
            for (StringBuilder sb : lines) {
                System.out.println(sb);
            }
        }

        // inventory (unchanged)
        System.out.println(Messages.INVENTORY_HEADER);
        var items = player.getInventory().getAllItems();
        if (items.isEmpty()) {
            System.out.println(Messages.INVENTORY_EMPTY);
        } else {
            for (var item : items) {
                System.out.printf(
                        Messages.INVENTORY_ITEM + "%n",
                        item.getName(),
                        item.getDescription()
                );
            }
        }
    }

    /**
     * Only edges of the map are closed, all interior walls are open.
     * row is 0-based; col is 1-based here.
     */
    private String defaultOpening(int row, int col, int size) {
        StringBuilder sb = new StringBuilder();
        if (row > 0) sb.append("N");  // Not on top edge
        if (col < size) sb.append("O");  // Not on right edge
        if (row < size-1) sb.append("Z");  // Not on bottom edge
        if (col > 1) sb.append("W");  // Not on left edge
        return sb.toString();
    }

    private String shorten(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max);
    }

    /** Center `text` inside a field of width `w`. */
    private String center(String text, int w) {
        int pad = w - text.length();
        int left = pad/2;
        int right = pad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }
}
