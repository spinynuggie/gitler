package org.example;

public class Monster {
    private final String name;
    private final int strength;
    private int health;
    private final HinderingStrategy attackStrategy;

    public Monster(String name, int strength, int health, HinderingStrategy attackStrategy) {
        this.name = name;
        this.strength = strength;
        this.health = health;
        this.attackStrategy = attackStrategy;
    }

    public void hinder(Player player) {
        attackStrategy.hinder(player, strength);
    }

    public void takeDamage(int dmg) {
        health = Math.max(health - dmg, 0);
        System.out.printf("🗡️  You hit %s for %d (Monster HP: %d)%n", name, dmg, health);
    }

    public boolean isAlive() {
        return health > 0;
    }
}
