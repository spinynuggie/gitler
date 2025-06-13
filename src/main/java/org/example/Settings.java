package org.example;

import java.util.Scanner;

public class Settings {
    public static void show(Scanner scanner) {
        while (true) {
            System.out.println(Messages.SETTINGS_HEADER);
            System.out.printf(Messages.SETTINGS_DEBUG + "%n", GeminiService.isDebug() ? "AAN" : "UIT");
            System.out.println(Messages.SETTINGS_BACK);
            System.out.print(Messages.MENU_KEUZE);
            String c = scanner.nextLine().trim();
            if ("1".equals(c)) {
                GeminiService.setDebug(!GeminiService.isDebug());
                System.out.printf(Messages.DEBUG_STATUS, GeminiService.isDebug() ? "AAN" : "UIT");
            } else if ("2".equals(c)) {
                System.out.println();
                return;
            } else {
                System.out.println(Messages.SETTINGS_INVALID);
            }
        }
    }
}
