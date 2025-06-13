package org.example;

public class AssistantHintProvider implements HintProvider {
    private static final String ASSISTANT_HINT_PROMPT = """
            Je bent een Scrum-master die een junior teamlid een hint geeft.
            Geef een hint voor de volgende vraag, maar geef niet het antwoord.
            De hint moet in het Nederlands zijn en niet meer dan 15 woorden.
            De vraag is: "{QUESTION}"
            """;

    @Override
    public String getHint(String question) {
        String prompt = ASSISTANT_HINT_PROMPT.replace("{QUESTION}", question);
        return GeminiService.askGemini(prompt);
    }
} 