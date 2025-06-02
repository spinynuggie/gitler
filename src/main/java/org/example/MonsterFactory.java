package org.example;

public class MonsterFactory {
    public static Monster createMonsterFor(Room room) {
        String name = room.name.toLowerCase();

        return switch (name) {
            case "daily scrum" -> new Monster("Scrum Gremlin", 2, 8, new AttackStrategy());
            case "sprint planning" -> new Monster("Planning Orc", 4, 12, new AttackStrategy());
            case "sprint review" -> new Monster("Feedback Fiend", 3, 10, new AttackStrategy());
            default -> new Monster("Generic Goblin", 3, 10, new AttackStrategy());
        };
    }
}
