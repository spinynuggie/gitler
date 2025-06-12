package org.example;

import java.util.Scanner;
@FunctionalInterface
public interface AssistantAction {
    void execute(Room room, Player player, Scanner scanner);
    static AssistantAction of(AssistantAction action) {
        return action;
    }
} 