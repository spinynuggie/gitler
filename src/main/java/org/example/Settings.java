package org.example;

import java.util.Scanner;

public class Settings {
    public static void show(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Settings ---");
            System.out.println("1. Toggle DEBUG (nu "
                    + (GeminiService.isDebug() ? "AAN" : "UIT") + ")");
            System.out.println("2. Terug");
            System.out.print("Maak een keuze: ");
            String c = scanner.nextLine().trim();
            if ("1".equals(c)) {
                GeminiService.setDebug(!GeminiService.isDebug());
                System.out.println("DEBUG staat nu "
                        + (GeminiService.isDebug() ? "AAN" : "UIT") + ".\n");
            } else if ("2".equals(c)) {
                System.out.println();
                return;
            } else {
                System.out.println("⚠️ Ongeldige keuze.\n");
            }
        }
    }
}
