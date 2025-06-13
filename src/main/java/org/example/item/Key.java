package org.example.item;

public class Key extends Item {
    public Key() {
        super("Key", "Unlocks a part of the exit. Collect them all!");
    }

    @Override
    public void use() {
        System.out.println("You look at the key. It seems important for the exit.");
    }
} 