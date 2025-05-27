package org.example;
import java.util.*;

public class GameMap {
    Random random = new Random();
    List<Room> kamers = new ArrayList<>();
    int randomGetalvijftotnegen;
    EvaluationStrategy gemini = new GeminiEvaluationStrategy();

    public GameMap() {
        randomGetalvijftotnegen = (random.nextInt(5) + 5);
        List<Integer> ids = new ArrayList<>();
        boolean floatingroom = true;

        while (floatingroom) {
            floatingroom = false;
            ids.clear();
            for (int i = 1; i <= 9; i++) {
                ids.add(i);
            }

            Collections.shuffle(ids);
            ids = new ArrayList<>(ids.subList(0, randomGetalvijftotnegen)); // Maak nieuwe lijst!

            // BFS om te checken of alles verbonden is
            Set<Integer> bezocht = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(ids.get(0));
            bezocht.add(ids.get(0));

            int[] offsets = {1, -1, 3, -3};
            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int offset : offsets) {
                    int buur = current + offset;
                    if (ids.contains(buur) && !bezocht.contains(buur) && zijnBuren(current, buur)) {
                        bezocht.add(buur);
                        queue.add(buur);
                    }
                }
            }

            if (bezocht.size() != ids.size()) {
                floatingroom = true; // Niet alles is verbonden, probeer opnieuw
            }
        }

        List<String> kamerOpening = new ArrayList<>();
        int randomKamerId = 0;
        if(ids.contains(5)){
            ids.remove(Integer.valueOf(5)); // Verwijder kamer 5 als deze bestaat
            int randomIndex = random.nextInt(ids.size());
            randomKamerId = ids.get(randomIndex);
            ids.add(5);
        }else{
            int randomIndex = random.nextInt(ids.size());
            randomKamerId = ids.get(randomIndex);
        }

        for (int kamerId : ids) {
            List<String> WASD = new ArrayList<>();
            if(kamerId == randomKamerId) {
                // Random kamer is 5, dus deze heeft geen buren
                WASD.add("x");
                if (kamerId >= 1 && kamerId <= 3) {
                    WASD.add("W");
                }
                // Onderste rij krijgt "S"
                if (kamerId >= 7 && kamerId <= 9) {
                    WASD.add("S");
                }
                // Linkerkolom krijgt "A"
                if (kamerId == 1 || kamerId == 4 || kamerId == 7) {
                    WASD.add("A");
                }
                // Rechterkolom krijgt "D"
                if (kamerId == 3 || kamerId == 6 || kamerId == 9) {
                    WASD.add("D");
                }
            }
            // Boven (W)
            if (ids.contains(kamerId - 3)) {
                WASD.add("W");
            }
            // Onder (S)
            if (ids.contains(kamerId + 3)) {
                WASD.add("S");
            }
            // Links (A)
            if (kamerId % 3 != 1 && ids.contains(kamerId - 1)) {
                WASD.add("A");
            }
            // Rechts (D)
            if (kamerId % 3 != 0 && ids.contains(kamerId + 1)) {
                WASD.add("D");
            }
            // Sla deze WASD-lijst op bij de kamer
            String bericht = kamerId+String.join("", WASD);
            kamerOpening.add(bericht);

        }

        for (int i = 0; i < randomGetalvijftotnegen; i++) {
            Question q = Questions.get(random.nextInt(Questions.size()));
            String randomvraag = q.getText();
            String randomnaam  = q.getName();

            if(kamerOpening.get(i).contains("x")){
                kamers.add(Room.of(ids.get(i), randomnaam , randomvraag , gemini, kamerOpening.get(i), true));
            }else{
                kamers.add(Room.of(ids.get(i), randomnaam , randomvraag , gemini, kamerOpening.get(i), false));
            }
        }
    }

    boolean zijnBuren(int a, int b) {
        if (Math.abs(a - b) == 1) {
            // Horizontale buren, check dat ze op dezelfde rij zitten
            return (a - 1) / 3 == (b - 1) / 3;
        } else if (Math.abs(a - b) == 3) {
            // Verticale buren
            return true;
        }
        return false;
    }


    public void viewMap(Player player) {
        System.out.println("🗺️  Map");
        for (int rij = 0; rij < 3; rij++) {
            // Eerste regel van de blokken
            for (int kol = 1; kol <= 3; kol++) {
                int id = rij * 3 + kol;
                Room gevonden = null;
                for (Room kamer : kamers) {
                    if (kamer.id == id) {
                        gevonden = kamer;
                        break;
                    }
                }
                if (gevonden != null) {
                    String open = gevonden.opening;
                    if(open.contains("W")){

                        System.out.printf("+----  ----+ ");
                    }else{
                        System.out.printf("+----------+ ");
                    }

                } else {
                    System.out.print("             ");
                }

            }
            System.out.println();

            // Tweede regel (naam of X)
            for (int kol = 1; kol <= 3; kol++) {
                int id = rij * 3 + kol;
                Room gevonden = null;
                for (Room kamer : kamers) {
                    if (kamer.id == id) {
                        gevonden = kamer;
                        break;
                    }
                }
                if (gevonden != null) {

                    System.out.printf("| %-9s| ", gevonden.name.length() > 9 ? gevonden.name.substring(0,9) : gevonden.name);

                } else {
                    System.out.print("             ");
                }
            }
            System.out.println();

            // Derde regel (kamer nummer of leeg)
            for (int kol = 1; kol <= 3; kol++) {
                int id = rij * 3 + kol;
                Room gevonden = null;
                for (Room kamer : kamers) {
                    if (kamer.id == id) {
                        gevonden = kamer;
                        break;
                    }
                }
                if (gevonden != null) {
                    String open = gevonden.opening;
                    if (open.contains("D") && open.contains("A")) {
                        System.out.printf("  kamer %-3d  ", gevonden.id);
                    } else if (open.contains("D")) {
                        System.out.printf("| kamer %-3d  ", gevonden.id);
                    } else if (open.contains("A")) {
                        System.out.printf("  kamer %-3d| ", gevonden.id);
                    } else {
                        System.out.printf("| kamer %-3d| ", gevonden.id);
                    }
                } else {
                    System.out.print("     X       ");
                }
            }
            System.out.println();

            // Vierde kammer (of player hier is of niet)
            for (int kol = 1; kol <= 3; kol++) {
                int id = rij * 3 + kol;
                boolean gevonden = false;
                boolean playerInRoom = false;
                for (Room kamer : kamers) {
                    if (kamer.id == id) {
                        gevonden = true;
                        break;
                    }
                }

                if (player.currentRoom == id) {
                    playerInRoom = true;

                }

                if (!gevonden) {
                    System.out.print("             ");
                } else if (playerInRoom) {
                    System.out.printf("|    you   | ");
                } else {
                    System.out.print("|          | ");
                }
            }
            System.out.println();

            for (int kol = 1; kol <= 3; kol++) {
                int id = rij * 3 + kol;
                Room gevonden = null;
                for (Room kamer : kamers) {
                    if (kamer.id == id) {
                        gevonden = kamer;
                        break;
                    }
                }
                if (gevonden != null) {
                    String open = gevonden.opening;
                    if(open.contains("S")){

                        System.out.printf("+----  ----+ ");
                    }else{
                        System.out.printf("+----------+ ");
                    }

                } else {
                    System.out.print("             ");
                }

            }
            System.out.println();
        }
    }
    public Room getRoomById(int id) {
        for (Room kamer : kamers) {
            if (kamer.id == id) return kamer;
        }
        return null;
    }
}