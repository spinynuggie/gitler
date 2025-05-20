package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player player = SaveManager.load();
        if (player == null) {
            player = new Player(1, 3);
        }

        while (true) {
            player = TitleScreen.show(scanner, player);
            GameLoop.start(scanner, player);
        }
    }
}
