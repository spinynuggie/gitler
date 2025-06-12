package org.example;

import java.util.*;

public class Game {
    public static void start(Scanner scanner, Player player) {
        GameMap gameMap = player.getMap();
        if (gameMap == null) {
            int gridSize = 3; // Change this for different levels
            long seed = new Random().nextLong();
            gameMap = new GameMap(gridSize, seed);
            player.setMap(gameMap);
        }
        Map<Integer, Monster> monstersPerRoom = new HashMap<>();

        while (true) {
            System.out.println(Messages.KAMER_KEUZE);
            gameMap.viewMap(player);
            System.out.printf(Messages.HP_SCORE, player.hp, player.score);
            System.out.print(Messages.KEUZE_PROMPT);

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println(Messages.TERUG_NAAR_MENU);
                SaveManager.save(player);
                return;
            }

            int roomId;
            try {
                roomId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ONGELDIGE_INVOER);
                continue;
            }

            Room selectedRoom = gameMap.getRoomById(roomId);
            if (selectedRoom == null) {
                System.out.printf(Messages.KAMER_BESTAAT_NIET + "%n", roomId);
                continue;
            }

            if (player.completedRooms.contains(roomId)) {
                player.currentRoom = roomId;
                System.out.println(Messages.KAMER_VOLTOOID);
                continue;
            }

            if (!gameMap.kanBewegen(player.currentRoom, roomId)) {
                System.out.printf(Messages.NIET_AANGRENZEND + "%n", roomId);
                continue;
            }

            Player.JokerResult jokerResult = player.vraagEnVerwerkJoker(scanner, "kamer");
            if (jokerResult == Player.JokerResult.GEBRUIKT) {
                player.currentRoom = roomId;
                player.completedRooms.add(roomId);
                player.score += 10;
                player.tryAcquireSword(roomId, gameMap.getSwordRoomId());
                SaveManager.save(player);
                continue;
            } else if (jokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
                continue;
            }

            Monster monster = monstersPerRoom.get(roomId);
            if (monster == null) {
                monster = MonsterFactory.createMonsterFor();
                monstersPerRoom.put(roomId, monster);
            }

            while (monster.isAlive()) {
                System.out.printf(Messages.MONSTER_ONTMOETING + "%n", monster.getName());

                // --- 5 unieke random vragen uit de juiste categorie per battle ---
                String kamerCategorie = selectedRoom.name; // kamernaam is nu de categorie
                List<org.example.questions.Question> vragenUitCategorie = org.example.Questions.byCategory(kamerCategorie);
                Collections.shuffle(vragenUitCategorie);
                List<String> vragenVoorDezeBattle = new ArrayList<>();
                for (int i = 0; i < Math.min(5, vragenUitCategorie.size()); i++) {
                    vragenVoorDezeBattle.add(vragenUitCategorie.get(i).getText());
                }

                int vraagIndex = 0;
                while (monster.isAlive() && vraagIndex < vragenVoorDezeBattle.size()) {
                    String vraag = vragenVoorDezeBattle.get(vraagIndex);
                    boolean turnTaken = false;
                    while (!turnTaken && monster.isAlive()) {
                        System.out.println(Messages.WAT_WIL_JE_DOEN);
                        System.out.println(Messages.OPTIE_FIGHT);
                        System.out.println(Messages.OPTIE_CHECK);
                        System.out.print(Messages.KEUZE_PROMPT);
                        String action = scanner.nextLine().trim();
                        if (action.equalsIgnoreCase("1") || action.equalsIgnoreCase("FIGHT")) {
                            System.out.println(Messages.ASSISTANT_INFO);
                            System.out.println("\n— " + vraag + " —");
                            String answer = scanner.nextLine().trim();

                            // Handle assistant command
                            if (answer.equalsIgnoreCase("gebruik assistent")) {
                                Assistant assistant = new Assistant(List.of(
                                    AssistantAction.of((room, _, _) -> {
                                        String hint = GeminiService.getHint(room.getVraag());
                                        System.out.println("[Gemini Hint] " + hint);
                                    })
                                ));
                                assistant.activate(selectedRoom, player, scanner);
                                System.out.print("Typ je antwoord: ");
                                answer = scanner.nextLine().trim();
                            }

                            // Handle joker during answering
                            if (answer.equalsIgnoreCase("joker")) {
                                Player.JokerResult answerJokerResult = player.vraagEnVerwerkJoker(scanner, "vraag");
                                if (answerJokerResult == Player.JokerResult.GEBRUIKT) {
                                    turnTaken = true;
                                    vraagIndex++;
                                    continue;
                                } else if (answerJokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
                                    System.out.print("Typ je antwoord: ");
                                    answer = scanner.nextLine().trim();
                                }
                            }

                            boolean correct = selectedRoom.evaluateAnswer(answer, vraag);
                            if (correct) {
                                int damage = player.getAttackDamage();
                                monster.takeDamage(damage);

                                if (!monster.isAlive()) {
                                    System.out.println(Messages.MONSTER_VERSLAGEN);
                                    player.tryAcquireSword(roomId, gameMap.getSwordRoomId());
                                    player.currentRoom = roomId;
                                    player.score += 10;
                                    player.completedRooms.add(roomId);
                                    System.out.printf(Messages.KAMER_SCORE, roomId);
                                    SaveManager.save(player);
                                    break;
                                } else {
                                    System.out.println(Messages.MONSTER_LEEFT);
                                }
                            } else {
                                monster.hinder(player);
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
                                    System.out.println(Messages.GAME_OVER);
                                    SaveManager.save(player);
                                    return;
                                }
                            }
                            turnTaken = true;
                            vraagIndex++;
                        } else if (action.equalsIgnoreCase("2") || action.equalsIgnoreCase("CHECK")) {
                            System.out.println("\n[MONSTER INFO]");
                            System.out.println(monster.getStats());
                            System.out.println();
                        } else {
                            System.out.println(Messages.ONGELDIGE_ACTIE);
                        }
                    }
                }
                // Battle eindigt na 5 vragen, ook als monster nog leeft
                if (vraagIndex >= vragenVoorDezeBattle.size() && monster.isAlive()) {
                    System.out.println(Messages.VRAGEN_OP);
                    break;
                }
            }
        }
    }
}