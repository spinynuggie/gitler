package org.example;

public class AssistantHintProvider extends AbstractHintProvider {

    private static final String ASSISTANT_HINT_PROMPT = """
            Jij bent een AI-assistent in een educatieve Scrum-game.
            Geef een directe, nuttige hint voor de volgende vraag.
            De speler heeft specifiek om jouw hulp gevraagd.
            VRAAG:
            """;
    @Override
    protected String getPrompt() {
        return ASSISTANT_HINT_PROMPT;
    }
}



