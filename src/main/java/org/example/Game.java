package org.example;

import java.util.Scanner;

public class Game {
    public static void start(Scanner scanner, Player player) {
        GameManager gameManager = new GameManager(scanner, player);
        gameManager.run();
    }
}