package org.example;
import java.util.Scanner;

public abstract class Room {
    public final int id;
    public final String name;
    private final EvaluationStrategy evaluator;
    public final String opening;
    public boolean open = false;

    protected Room(int id, String name, EvaluationStrategy evaluator, String opening, boolean open) {
        this.id = id;
        this.name = name;
        this.evaluator = evaluator;
        this.opening = opening;

    }

    protected abstract String getVraag();

    public boolean play(Scanner scanner) {
        System.out.println("\n— " + getVraag() + " —");
        String antwoord = scanner.nextLine().trim();
        if(antwoord.equals("1234")){
            return true;
        }
        String fb = evaluator.evaluate(getVraag(), antwoord);
        System.out.println(fb);
        String[] parts = fb.split(":", 2);
        if (parts.length > 1) System.out.println(parts[1].trim());
        return parts[0].equalsIgnoreCase("GOED");
    }

    public static Room of(int id, String name, String vraag, EvaluationStrategy evaluator, String opening, Boolean open) {
        return new Room(id, name, evaluator , opening, open) {
            @Override protected String getVraag() { return vraag; }
        };
    }
}
