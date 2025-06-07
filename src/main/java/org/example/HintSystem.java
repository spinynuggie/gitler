package org.example;

import java.util.Scanner;

public class HintSystem {
    public static void maybeGiveHint(Scanner scanner, String question) {
        System.out.print("❔ Wil je een hint? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            HintProvider provider = HintFactory.getRandomHintProvider();
            System.out.println(provider.getHint(question));
        } else {
            System.out.println("🔕 Geen hint gekozen.");
        }
    }
}