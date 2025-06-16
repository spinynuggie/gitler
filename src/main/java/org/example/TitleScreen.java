package org.example;

import java.util.Scanner;

public class TitleScreen {
    private static final int STARTING_HP = 10;  // match your Main.java default
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
            System.out.print(Messages.MENU_KEUZE);

            String keuze = scanner.nextLine().trim();
            if (keuze.equals("admin42")) {
                AdminPanel.show(scanner);
                continue;
            }
            switch (keuze) {
                case "1":
                    // start the game with whatever Player we currently have
                    return player;

                case "2":
                    SaveManager.save(player);
                    System.out.println(Messages.GAME_OPGESLAGEN);
                    break;

                case "3":
                    System.out.print(Messages.RESET_BEVESTIGING);
                    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                        // delete old save
                        SaveManager.reset();
                        // re-initialize to starting HP
                        player = new Player(STARTING_HP);
                        // immediately save this fresh state
                        SaveManager.save(player);
                        System.out.printf(Messages.RESET_VOLTOOID, player.getHp());
                    }
                    break;

                case "4":
                    Settings.show(scanner);
                    break;

                case "5":
                    System.out.println(Messages.CONTROLS_HEADER);
                    System.out.println(Messages.CONTROLS_KAMER);
                    System.out.println(Messages.CONTROLS_SAVE);
                    System.out.println(Messages.CONTROLS_RESET);
                    System.out.println(Messages.CONTROLS_EXIT);
                    break;

                case "6":
                    System.out.println(Messages.HELP_HEADER);
                    System.out.println(Messages.HELP_UITLEG);
                    System.out.println(Messages.HELP_AI);
                    break;

                case "7":
                    System.out.println(Messages.TOT_ZIENS);
                    System.exit(0);
                    break;  // unreachable

                default:
                    System.out.println(Messages.ONGELDIGE_MENU_KEUZE);
            }
        }
    }
}
