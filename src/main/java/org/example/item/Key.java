package org.example.item;

public class Key extends Item {
    public Key() {
        super("Sleutel", "Ontgrendelt de EXIT. Vind alle sleutels om te winnen!");
    }

    @Override
    public void use() {
        System.out.println("Je kijkt naar de sleutel.");
    }
} 