package org.example.item;

public class Sword extends Item {
    private final int damage;

    public Sword() {
        super("Zwaard", "Een krachtig zwaard dat extra schade doet aan monsters");
        this.damage = 3; // Zelfde schade als voorheen
    }

    @Override
    public void use() {
        // Zwaard is een passief item, wordt automatisch gebruikt in gevechten
    }

    public int getDamage() {
        return damage;
    }
} 