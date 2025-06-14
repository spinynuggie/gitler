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

        // 2. Voeg een educatief hulpmiddel toe (placeholder)
        System.out.println("[Educatief Hulpmiddel] Een stappenplan om deze vraag te beantwoorden is aan je notities toegevoegd.");

        // 3. Toon een motiverende boodschap
        System.out.println("[Motivatie] Je denkt als een echte product owner!");
    }
} 