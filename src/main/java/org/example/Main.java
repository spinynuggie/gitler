package org.example;

import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player player = SaveManager.load();
        if (player == null) {
            player = new Player(1);
        }
        
        // Initialize map if it's not already set
        if (player.getMap() == null) {
            int gridSize = 3; // Default grid size
            long seed = new Random().nextLong();
            GameMap gameMap = new GameMap(gridSize, seed);
            player.setMap(gameMap);
        }

        while (true) {
            player = TitleScreen.show(scanner, player);
            Game.start(scanner, player);
        }
    }
}