package org.example;

import java.util.Scanner;

public class MotivationalMessageAction implements AssistantAction {
    private static final String[] MESSAGES = {
        "Je denkt als een echte product owner!",
        "Goed bezig, hou vol!",
        "Je bent op de goede weg!"
    };
    @Override
    public void execute(Room room, Player player, Scanner scanner) {
        System.out.println("Motivatie: " + MESSAGES[new java.util.Random().nextInt(MESSAGES.length)]);
    }
} 