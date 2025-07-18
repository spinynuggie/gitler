package org.example;

import org.example.item.Item;

import java.io.Serial;
import java.io.Serializable;

public class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public final int id;
    public final String name;
    private final EvaluationStrategy evaluator;
    public final String opening;
    private final String vraag;
    private Item item;

    private Room(int id, String name, String vraag, EvaluationStrategy evaluator, String opening) {
        this.id = id;
        this.name = name;
        this.evaluator = evaluator;
        this.opening = opening;
        this.vraag = vraag;
        this.item = null;
    }

    public String getVraag() {
        return vraag;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Item takeItem() {
        Item pickedItem = this.item;
        this.item = null;
        return pickedItem;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public EvaluationStrategy getEvaluator() {
        return evaluator;
    }

    // Factory method
    public static Room of(int id, String name, String vraag, EvaluationStrategy evaluator, String opening) {
        return new Room(id, name, vraag, evaluator, opening);
    }
}
