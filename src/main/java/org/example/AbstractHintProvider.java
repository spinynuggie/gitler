package org.example;

public abstract class AbstractHintProvider implements HintProvider {

    protected abstract String getPrompt();

    @Override
    public String getHint(String question) {
        String prompt = getPrompt();
        return GeminiService.askGemini(prompt + question);
    }
} 