package org.example;

import java.util.Scanner;

public abstract class Room {
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

    // Nieuwe overload zodat je een specifieke vraag kunt stellen
    public boolean play(Scanner scanner, Player player, String vraag) {
        // Toon de vraag
        System.out.println("\n— " + vraag + " —");
        String antwoord = scanner.nextLine().trim();

        // Assistent-commando
        if (antwoord.equalsIgnoreCase("gebruik assistent")) {
            Assistant assistant = new Assistant(java.util.List.of(
                new HintAction(),
                new EducationalAidAction(),
                new MotivationalMessageAction()
            ));
            assistant.activate(this, player, scanner);
            // Vraag opnieuw om antwoord
            System.out.print("Typ je antwoord: ");
            antwoord = scanner.nextLine().trim();
        }

        // Joker‐check tijdens het beantwoorden
        if (antwoord.equalsIgnoreCase("joker")) {
            Player.JokerResult jokerResult = player.vraagEnVerwerkJoker(scanner, "vraag");
            if (jokerResult == Player.JokerResult.GEBRUIKT) {
                return true;
            } else if (jokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
                System.out.print("Typ je antwoord: ");
                antwoord = scanner.nextLine().trim();
            }
        }

        // Shortcut (zoals je vroeger '1234' gebruikte)
        if (antwoord.equals("1234")) {
            return true;
        }

        // Normale evaluatie via je evaluator-strategie
        String fb = evaluator.evaluate(vraag, antwoord);
        String[] parts = fb.split(":", 2);
        boolean correct = parts[0].equals("GOED");
        System.out.println(fb); // print eenmalig de GOED: ... of FOUT: ...
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

    // Geeft een educatief hulpmiddel terug (kan per kamer overschreven worden)
    public String getEducationalAid() {
        return "Stappenplan: Lees de vraag goed, denk na, en geef een zo volledig mogelijk antwoord.";
    }
}
