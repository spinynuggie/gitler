package org.example;

import java.util.List;
import java.util.Random;

public class MonsterFactory {
    private static final List<MonsterTemplate> MONSTER_TEMPLATES = List.of(
            new MonsterTemplate("Geert Wulders", 3, 2),
            new MonsterTemplate("Joseph Stalout", 2, 4),
            new MonsterTemplate("Thierry Boudet", 3, 2),
            new MonsterTemplate("Joe Biben", 2, 3),
            new MonsterTemplate("Donald Trunt", 3, 2),
            new MonsterTemplate("Dik Schoen", 2, 4),
            new MonsterTemplate("Mark Ruttel", 3, 3),
            new MonsterTemplate("Vladimir Input", 4, 4) // FINAL BOSS 🔥
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
