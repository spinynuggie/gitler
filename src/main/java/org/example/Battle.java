package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Battle {

    private final Scanner scanner;
    private final Player player;
    private final Monster monster;
    private final Room room;
    private final GameMap gameMap;
    private final Assistant assistant;

    public enum BattleResult {
        WIN,
        DEFEAT,
        FLEE
    }

    public Battle(Scanner scanner, Player player, Monster monster, Room room, GameMap gameMap, Assistant assistant) {
        this.scanner = scanner;
        this.player = player;
        this.monster = monster;
        this.room = room;
        this.gameMap = gameMap;
        this.assistant = assistant;
    }

    public BattleResult start() {
        System.out.printf(Messages.MONSTER_ONTMOETING + "%n", monster.getName());

        String kamerCategorie = room.name;
        List<org.example.questions.Question> vragenUitCategorie = org.example.Questions.byCategory(kamerCategorie);
        Collections.shuffle(vragenUitCategorie);
        List<String> vragenVoorDezeBattle = new ArrayList<>();
        for (int i = 0; i < Math.min(5, vragenUitCategorie.size()); i++) {
            vragenVoorDezeBattle.add(vragenUitCategorie.get(i).getText());
        }

        int vraagIndex = 0;
        while (monster.isAlive() && vraagIndex < vragenVoorDezeBattle.size()) {
            TurnResult turnResult = handleTurn(vragenVoorDezeBattle.get(vraagIndex), false);
            
            if (turnResult == TurnResult.FLED) {
                return BattleResult.FLEE;
            }

            if (turnResult == TurnResult.FOUGHT) {
                 vraagIndex++;
            }

            if (player.hp <= 0) {
                return BattleResult.DEFEAT;
            }
        }
        
        if (!monster.isAlive()) {
            return BattleResult.WIN;
        }

        if (vraagIndex >= vragenVoorDezeBattle.size()) {
            System.out.println(Messages.VRAGEN_OP);
        }
        
        // Als de vragen op zijn maar het monster leeft nog, beschouw het als een vlucht.
        return BattleResult.FLEE;
    }

    public void startFinalBossBattle() {
        System.out.printf(Messages.MONSTER_ONTMOETING + "%n", monster.getName());

        List<org.example.questions.Question> allQuestions = getAllQuestions();
        Collections.shuffle(allQuestions);
        List<String> vragenVoorDezeBattle = new ArrayList<>();
        for (int i = 0; i < Math.min(25, allQuestions.size()); i++) { // Boss has 25 HP, so up to 25 questions
            vragenVoorDezeBattle.add(allQuestions.get(i).getText());
        }

        int vraagIndex = 0;
        while (monster.isAlive() && vraagIndex < vragenVoorDezeBattle.size()) {
            TurnResult turnResult = handleTurn(vragenVoorDezeBattle.get(vraagIndex), true);

            if (turnResult == TurnResult.FOUGHT) {
                vraagIndex++;
            }

            if (player.hp <= 0) {
                return; // Exit battle if player is defeated
            }
        }

        if (vraagIndex >= vragenVoorDezeBattle.size() && monster.isAlive()) {
            System.out.println(Messages.VRAGEN_OP);
        }
    }

    private List<org.example.questions.Question> getAllQuestions() {
        List<org.example.questions.Question> allQuestions = new ArrayList<>();
        for (String category : Questions.allCategories()) {
            allQuestions.addAll(Questions.byCategory(category));
        }
        return allQuestions;
    }

    private enum TurnResult { FOUGHT, FLED, CHECKED, JOKER_USED }

    public TurnResult handleTurn(String vraag, boolean isFinalBoss) {
        while (true) {
            System.out.println(Messages.WAT_WIL_JE_DOEN);
            System.out.println(Messages.OPTIE_FIGHT);
            System.out.println(Messages.OPTIE_CHECK);
            if (!isFinalBoss) {
                System.out.println("3. FLEE");
            }
            System.out.print(Messages.KEUZE_PROMPT);
            String action = scanner.nextLine().trim();

            if (action.equalsIgnoreCase("1") || action.equalsIgnoreCase("FIGHT")) {
                handleFight(vraag);
                return TurnResult.FOUGHT;
            } else if (action.equalsIgnoreCase("2") || action.equalsIgnoreCase("CHECK")) {
                System.out.println("\n[MONSTER INFO]");
                System.out.println(monster.getStats());
                System.out.println();
                return TurnResult.CHECKED;
            } else if (!isFinalBoss && (action.equalsIgnoreCase("3") || action.equalsIgnoreCase("FLEE"))) {
                System.out.println(Messages.FLEE_SUCCESS);
                return TurnResult.FLED;
            } else {
                System.out.println(Messages.ONGELDIGE_ACTIE);
            }
        }
    }

    private void handleFight(String vraag) {
        String answer = getPlayerAnswer(vraag);
        if (answer == null) return; // Joker used, turn skipped

        if (answer.equals("1234")) {
            System.out.println("DEBUG: Monster verslagen met cheatcode.");
            monster.takeDamage(999);
            return;
        }

        boolean correct = evaluateAnswer(vraag, answer);

        if (correct) {
            processCorrectAnswer();
        } else {
            processIncorrectAnswer(vraag);
        }
    }

    private String getPlayerAnswer(String vraag) {
        System.out.println(Messages.ASSISTANT_INFO);
        System.out.println("\n— " + vraag + " —");
        String answer = scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("gebruik assistent")) {
            assistant.activate(vraag);
            System.out.print("Typ je antwoord: ");
            answer = scanner.nextLine().trim();
        }

        if (answer.equalsIgnoreCase("joker")) {
            Player.JokerResult answerJokerResult = player.vraagEnVerwerkJoker(scanner, "vraag");
            if (answerJokerResult == Player.JokerResult.GEBRUIKT) {
                return null; // Signal that the turn is skipped
            } else if (answerJokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
                System.out.print("Typ je antwoord: ");
                answer = scanner.nextLine().trim();
            }
        }
        return answer;
    }

    private boolean evaluateAnswer(String vraag, String answer) {
        if (room != null) {
            return room.evaluateAnswer(answer, vraag);
        } else {
            // Final boss battle, no specific room. Use a generic evaluation.
            String evaluation = new GeminiEvaluationStrategy().evaluate(vraag, answer);
            System.out.println(evaluation);
            return evaluation.startsWith("GOED");
        }
    }

    private void processCorrectAnswer() {
        int damage = player.getAttackDamage();
        monster.takeDamage(damage);

        if (!monster.isAlive()) {
            System.out.println(Messages.MONSTER_VERSLAGEN);
        } else {
            System.out.println(Messages.MONSTER_LEEFT);
        }
    }

    private void processIncorrectAnswer(String vraag) {
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
        }
    }
} 