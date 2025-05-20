package org.example;

import java.util.*;


public class GameMap {
    private static final int GRID = 5, CELLS = GRID * GRID;
    private static final Random RNG = new Random();
    private final Map<Integer,Room> rooms = new HashMap<>();
    private final EvaluationStrategy strat = new GeminiEvaluationStrategy();

    public GameMap() {
        Map<Integer,List<String>> pool = Map.of(
                7, List.of(
                        "Wat is de rol van de PO?",
                        "Noem 2 taken van de PO.",
                        "Waarom is prioriteren belangrijk?"
                ),
                12, List.of(
                        "Wat bespreek je tijdens een Daily Scrum?",
                        "Hoe lang duurt een Daily Scrum maximaal?",
                        "Wie leidt de Daily Scrum?"
                ),
                19, List.of(
                        "Wat toon je tijdens de Sprint Review?",
                        "Waarom betrek je stakeholders?",
                        "Wat is het doel van de Review?"
                )
        );
        List<Integer> available = new ArrayList<>(pool.keySet());
        int N = RNG.nextInt(5) + 5;
        Collections.shuffle(available, RNG);
        List<Integer> selected = available.subList(0, Math.min(N, available.size()));

        // 3) ensure connectivity (you can re-shuffle until allHaveNeighbors(selected))
        //    or skip that if you don't mind “floating” rooms in a 5×5.

        // 4) build Room instances
        for (int id : selected) {
            rooms.put(id, Room.of(id, "Kamer " + id, pool.get(id), strat));
        }
    }

    public Room getRoom(int id) {
        return rooms.get(id);
    }

    /**
     * Prints a 5×5 grid, marks the player with 'P', rooms by their ID, and empty tiles as 'X'.
     */
    public void viewMap(int playerPos) {
        int grid = 5;
        String hor = "+" + "-".repeat(4);

        // Top border
        for (int i = 0; i < grid; i++) System.out.print(hor);
        System.out.println("+");

        for (int row = 0; row < grid; row++) {
            // Cells
            for (int col = 0; col < grid; col++) {
                int id = row * grid + col + 1;
                String cell;

                if (id == playerPos) {
                    cell = " P ";                    // Player
                } else if (rooms.containsKey(id)) {
                    cell = String.format("%2d ", id); // A real room
                } else {
                    cell = " X ";                    // Empty
                }

                System.out.print("|" + cell);
            }
            System.out.println("|");

            // Separator
            for (int i = 0; i < grid; i++) System.out.print(hor);
            System.out.println("+");
        }
    }
}
