package org.example;

import java.util.Scanner;

public class TitleScreen {
    // show the main menu and return the (possibly new) Player
    public static Player show(Scanner scanner, Player player) {
        final String BOLD  = "\u001B[1m";
        final String RESET = "\u001B[0m";
        String title = BOLD + """
                   /|                                   /|
                  / |                                  / |      ___
                 /  |                                 /  |      | |
                /___ ‾‾‾‾/  __ _ __ _   _ _ __ ___   /___ ‾‾‾‾/ | |_ __ _ _ __ ___
                    |  /  / __| '__| | | | '_ ` _ \\      |  /   | __/ _` | '__/ __|
                    | /  | (__| |  | |_| | | | | | |     | /    | || (_| | |  \\__ \\
                    |/    \\___|_|   \\__,_|_| |_| |_|     |/     \\__ \\__,_|_|  |___/
                """ + RESET;

        while (true) {
            System.out.println(title);
            System.out.println("╔════════════════════════════╗");
            System.out.println("║ 1. Start Game              ║");
            System.out.println("║ 2. Save Game               ║");
            System.out.println("║ 3. Reset Save              ║");
            System.out.println("║ 4. Settings                ║");
            System.out.println("║ 5. Controls                ║");
            System.out.println("║ 6. Help                    ║");
            System.out.println("║ 7. Exit                    ║");
            System.out.println("╚════════════════════════════╝");
            System.out.print("Maak een keuze: ");

            String keuze = scanner.nextLine().trim();
            switch (keuze) {
                case "1":
                    return player;
                case "2":
                    SaveManager.save(player);
                    System.out.println("📂 Game opgeslagen!");
                    break;
                case "3":
                    System.out.print("⚠️ Weet je zeker dat je reset wilt? (y/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                        SaveManager.reset();
                        player = new Player(1, 3);
                        System.out.println("🗑️  Save gewist en nieuwe speler gestart!");
                    }
                    break;
                case "4":
                    Settings.show(scanner);
                    break;
                case "5":
                    System.out.println("\n🕹️  Controls:");
                    System.out.println(" - Typ het kamernummer om te spelen");
                    System.out.println(" - Typ 'save' om handmatig op te slaan");
                    System.out.println(" - Typ 'reset' om opnieuw te beginnen");
                    System.out.println(" - Typ 'exit' om terug te gaan naar dit menu\n");
                    break;
                case "6":
                    System.out.println("\n❓ Help:");
                    System.out.println("Beantwoord in elke kamer de vraag zo kort mogelijk.");
                    System.out.println("De AI geeft GOED of FOUT met één zin toelichting.\n");
                    break;
                case "7":
                    System.out.println("👋 Tot ziens!");
                    System.exit(0);
                default:
                    System.out.println("⚠️ Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
}
