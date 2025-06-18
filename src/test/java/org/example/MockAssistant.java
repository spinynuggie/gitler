package org.example;

// Een Mock is een object dat we gebruiken om interacties te verifiëren.
// We willen controleren OF de 'activate' methode wordt aangeroepen, niet wat het doet.
public class MockAssistant extends Assistant {

    private boolean activateCalled = false;
    private String questionPassed;

    public MockAssistant() {
        // We geven null mee aan de super constructor omdat we de echte HintProvider niet nodig hebben voor deze mock.
        // De 'activate' methode wordt overschreven.
        super(null);
    }

    @Override
    public void activate(String question) {
        this.activateCalled = true;
        this.questionPassed = question;
    }

    public boolean isActivateCalled() {
        return activateCalled;
    }

    public String getQuestionPassed() {
        return questionPassed;
    }
} 