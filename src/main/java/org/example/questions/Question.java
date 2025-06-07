package org.example.questions;

public class Question {
    private final String category;
    private final String text;
    public Question(String category, String text) {
        this.category = category;
        this.text = text;
    }
    public String getCategory() { return category; }
    public String getText() { return text; }
} 