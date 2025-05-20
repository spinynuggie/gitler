package org.example;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Room {
    public final int id;
    public final String name;
    private final List<String> questions;
    private final EvaluationStrategy strategy;
    private final Random rng = new Random();

    private Room(int id, String name, List<String> questions, EvaluationStrategy strategy) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.strategy = strategy;
    }

    public static Room of(int id,
                          String name,
                          List<String> questions,
                          EvaluationStrategy strategy) {
        return new Room(id, name, questions, strategy);
    }

    public boolean play(Scanner scanner) {
        // pick a random question…
        String q = questions.get(rng.nextInt(questions.size()));
        System.out.println("\n👾 Monster verschijnt in “" + name + "”!");
        System.out.println("— " + q + " —");

        String answer = scanner.nextLine().trim();
        // get the full AI feedback as String
        String feedback = strategy.evaluate(q, answer);
        // show the user what Gemini said
        System.out.println(feedback);
        // return true if it starts with “GOED”
        return feedback.startsWith("GOED");
    }
}
