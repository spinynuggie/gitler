package org.example;

import java.util.Scanner;

public class Prologue {

    public Prologue() {
    }

    public void show(Scanner scanner) {
        System.out.println("\n--- PROLOOG ---\n");
        String text = "Welkom bij het avontuur! Bereid je voor op een spannend spel.";
        System.out.println(text);
        System.out.println("\nDruk op Enter om verder te gaan...");
        scanner.nextLine();
    }
} 