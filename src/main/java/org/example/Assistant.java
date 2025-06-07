package org.example;

import java.util.List;
import java.util.Scanner;

public class Assistant {
    private final List<AssistantAction> actions;

    public Assistant(List<AssistantAction> actions) {
        this.actions = actions;
    }

    public void activate(Room room, Player player, Scanner scanner) {
        for (AssistantAction action : actions) {
            action.execute(room, player, scanner);
        }
    }
} 