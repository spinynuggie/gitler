package org.example.item;

public class Sword extends Item {
    private final int damage;

    public Sword() {
        super("Zwaard", "Een krachtig zwaard dat extra schade doet aan monsters");
        this.damage = 3;
    }

    @Override
    public void use() {
    }

    public int getDamage() {
        return damage;
    }
} 