package org.example;

import java.io.Serial;
import java.io.Serializable;

public abstract class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public final int id;
    public final String name;
    private final EvaluationStrategy evaluator;
    public final String opening;

    protected Room(int id, String name, EvaluationStrategy evaluator, String opening) {
        this.id = id;
        this.name = name;
        this.evaluator = evaluator;
        this.opening = opening;
    }

    // 1) Houd alleen deze abstracte definitie over:
    protected abstract String getVraag();

    // Simplified play method that only handles evaluation
    public boolean evaluateAnswer(String answer, String currentVraag) {
        if (answer.equals("1234")) { // Keep debug/testing shortcut
            return true;
        }

        String fb = evaluator.evaluate(currentVraag, answer);
        String[] parts = fb.split(":", 2);
        boolean correct = parts[0].equals("GOED");
        System.out.println(fb);
        return correct;
    }

    // Laat deze factory‐methode precies staan zoals-ie was:
    public static Room of(int id, String name, String vraag, EvaluationStrategy evaluator, String opening) {
        return new Room(id, name, evaluator, opening) {
            @Override
            protected String getVraag() {
                return vraag;
            }
        };
    }
}
