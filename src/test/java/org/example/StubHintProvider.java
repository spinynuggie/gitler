package org.example;

// Een Stub is een object dat een vaste, voorspelbare waarde teruggeeft.
// We gebruiken dit om de reactie van ons systeem te testen zonder de echte afhankelijkheid (GeminiService) aan te roepen.
public class StubHintProvider extends AbstractHintProvider {

    private final String fixedHint;

    public StubHintProvider(String fixedHint) {
        this.fixedHint = fixedHint;
    }

    @Override
    protected String getPrompt() {
        // De prompt is hier niet relevant, omdat we de getHint methode overschrijven.
        return "";
    }

    @Override
    public String getHint(String question) {
        // Geef altijd de vaste hint terug, ongeacht de vraag.
        return fixedHint;
    }
} 