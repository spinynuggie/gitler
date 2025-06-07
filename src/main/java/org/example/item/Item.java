package org.example.item;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private final String name;
    private final String description;

    protected Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void use();
} 