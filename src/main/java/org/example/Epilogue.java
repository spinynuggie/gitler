package org.example;

import java.util.Scanner;

public class Epilogue {

    public Epilogue() {
    }

    public void show(Scanner scanner) {
        System.out.println("\n--- EPILOOG ---\n");
        String text = "Bedankt voor het spelen! Hopelijk heb je genoten van het spel.";
        System.out.println(text);
        System.out.println("\nDruk op Enter om af te sluiten...");
        scanner.nextLine();
    }

    public static void showWin(Scanner scanner) {
        Epilogue epilogue = new Epilogue();
        epilogue.show(scanner);
    }
} 