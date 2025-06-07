package org.example.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory implements Serializable {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean hasItem(Class<? extends Item> itemType) {
        return items.stream().anyMatch(itemType::isInstance);
    }

    public <T extends Item> Optional<T> getItem(Class<T> itemType) {
        return items.stream()
                .filter(itemType::isInstance)
                .map(itemType::cast)
                .findFirst();
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
} 