package org.example.item;

public class Joker extends Item {
    private boolean used = false;

    public Joker() {
        super("Joker", "Een magische kaart waarmee je een kamer kunt overslaan");
    }

    @Override
    public void use() {
        if (!used) {
            used = true;
        }
    }

    public boolean isAvailable() {
        return !used;
    }
} 