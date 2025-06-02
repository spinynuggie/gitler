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

    // 2) Pas hier alleen de play-methode aan; verwijder alle andere getVraag‐overrides:
    public boolean play(Scanner scanner, Player player) {
        // Haal de vraag één keer op via de abstracte methode
        String vraag = getVraag();

        // Toon de vraag
        System.out.println("\n— " + vraag + " —");
        String antwoord = scanner.nextLine().trim();

        // Joker‐check tijdens het beantwoorden
        if (antwoord.equalsIgnoreCase("joker")) {
            if (!player.jokerAvailable) {
                System.out.println("❌ Je hebt je Joker al gebruikt! Probeer alsnog de vraag te beantwoorden:");
                System.out.print("Typ je antwoord: ");
                antwoord = scanner.nextLine().trim();
            } else {
                player.jokerAvailable = false;
                System.out.println("🃏 Joker gebruikt! Vraag automatisch goed gerekend.");
                return true;
            }
        }

        // Shortcut (zoals je vroeger ‘1234’ gebruikte)
        if (antwoord.equals("1234")) {
            return true;
        }

        // Normale evaluatie via je evaluator-strategie
        String fb = evaluator.evaluate(getVraag(), antwoord);
        String[] parts = fb.split(":", 2);
        if (parts.length > 1) {
            System.out.println(fb); // print eenmalig de GOED: ... of FOUT: ...
            return parts[0].equalsIgnoreCase("GOED");
        } else {
            System.out.println("⚠️ Antwoord niet herkend.");
            return false;
        }
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
