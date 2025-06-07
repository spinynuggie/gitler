package org.example;

import java.util.Scanner;

public class EducationalAidAction implements AssistantAction {
    @Override
    public void execute(Room room, Player player, Scanner scanner) {
        System.out.println("Educatief hulpmiddel: " + room.getEducationalAid());
    }
} 