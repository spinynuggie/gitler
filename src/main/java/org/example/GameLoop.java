package org.example;

import java.util.*;

public class GameLoop {
    /**
     * The old Main.startGameLoop logic, now in its own class.
     */
    public static void start(Scanner scanner, Player player) {
        EvaluationStrategy gemini = new GeminiEvaluationStrategy();
        List<Room> rooms = List.of(
                Room.of(1, "Sprint Planning",   "Wat is de rol van de PO?", gemini),
                Room.of(2, "Daily Scrum",       "Wat bespreek je tijdens een Daily Scrum?", gemini),
                Room.of(3, "Sprint Review",     "Wat toon je tijdens de Sprint Review?", gemini)
        );

        Map<Integer, Room> roomMap = new HashMap<>();
        for (Room r : rooms) roomMap.put(r.id, r);

        while (true) {
            System.out.println("\n🎯 Kies een kamer (of typ 'exit'):");
            for (Room r : rooms) {
                boolean done = player.completedRooms.contains(r.id);
                String mark = done ? "✓" : " ";
                System.out.printf("  %d. [%s] %s%n",
                        r.id, mark, r.name);
            }
            System.out.printf("%n📍 HP: %d | Score: %d%n",
                    player.hp, player.score);
            System.out.print("Keuze: ");

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("↩️  Terug naar hoofdmenu...");
                return;
            }

            int roomId;
            try {
                roomId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Ongeldige invoer. Probeer opnieuw.");
                continue;
            }

            int highestDone = player.completedRooms.isEmpty()
                    ? 0
                    : Collections.max(player.completedRooms);
            int maxAllowed = highestDone + 1;
            if (roomId > maxAllowed) {
                System.out.println("🔒 Kamer " + roomId
                        + " is vergrendeld. Voltooi eerst kamer " + highestDone + ".");
                continue;
            }

            if (player.completedRooms.contains(roomId)) {
                System.out.print("⚠️ Kamer " + roomId
                        + " al voltooid. Opnieuw? (y/n): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    System.out.println("→ Terug naar kamer-keuze.");
                    continue;
                }
            }

            boolean correct = roomMap.get(roomId).play(scanner);
            if (correct) {
                player.currentRoom = roomId;
                player.score += 10;
                player.completedRooms.add(roomId);
                System.out.println("✅ Goed! +10 score");
                System.out.printf("Voortgang: kamer %d | Score: %d | HP: %d%n",
                        player.currentRoom, player.score, player.hp);
                SaveManager.save(player);
            } else {
                player.hp--;
                System.out.println("❌ Fout! -1 HP!");
                if (player.hp <= 0) {
                    System.out.println("\n💀 Je hebt geen HP meer. Game over!");
                    return;
                }
            }
        }
    }
}
