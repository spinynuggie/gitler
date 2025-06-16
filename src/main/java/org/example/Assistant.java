package org.example;

public class Assistant {

    private final HintProvider hintProvider;

    public Assistant(HintProvider hintProvider) {
        this.hintProvider = hintProvider;
    }

    public void activate(String question) {
        // 1. Toon een hint
        String hint = hintProvider.getHint(question);
        System.out.println("[Gemini Hint] " + hint);
    }
} 