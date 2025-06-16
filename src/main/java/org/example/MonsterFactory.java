package org.example;

import java.util.List;
import java.util.Random;

public class MonsterFactory {
    private static final List<MonsterTemplate> MONSTER_TEMPLATES = List.of(
        new MonsterTemplate("Geert Wilders", 2, 3),
        new MonsterTemplate("Joseph Stalin", 4, 5),
        new MonsterTemplate("Thierry Baudet", 3, 4),
        new MonsterTemplate("Joe Biden", 3, 4),
        new MonsterTemplate("Donald Trump", 2, 3),
        new MonsterTemplate("Dik Schoof", 3, 5),
        new MonsterTemplate("Mark Rutte", 2, 4)
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
