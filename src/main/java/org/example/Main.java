package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player player = SaveManager.load();
        player = TitleScreen.show(scanner, player);
        Game.start(scanner, player);
    }
}