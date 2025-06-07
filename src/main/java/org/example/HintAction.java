package org.example;

import java.util.Scanner;

public class HintAction implements AssistantAction {
    @Override
    public void execute(Room room, Player player, Scanner scanner) {
        HintSystem.maybeGiveHint(scanner, room.getVraag());
    }
} 