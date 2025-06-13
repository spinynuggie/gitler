package org.example;

import java.util.List;
import java.util.Random;

public class MonsterFactory {
    private static final List<MonsterTemplate> MONSTER_TEMPLATES = List.of(
        new MonsterTemplate("Scrum Gremlin", 2, 3),
        new MonsterTemplate("Planning Orc", 4, 5),
        new MonsterTemplate("Feedback Fiend", 3, 4),
        new MonsterTemplate("Generic Goblin", 3, 4),
        new MonsterTemplate("Kanban Kobold", 2, 3),
        new MonsterTemplate("Retro Wraith", 3, 5),
        new MonsterTemplate("Agile Imp", 2, 4)
    );
    private static final Random RANDOM = new Random();

    public static Monster createMonsterFor() {
        MonsterTemplate template = MONSTER_TEMPLATES.get(RANDOM.nextInt(MONSTER_TEMPLATES.size()));
        return new Monster(template.name, template.strength, template.health, new AttackStrategy());
    }

    private static class MonsterTemplate {
        final String name;
        final int strength;
        final int health;
        MonsterTemplate(String name, int strength, int health) {
            this.name = name;
            this.strength = strength;
            this.health = health;
        }
    }
}
