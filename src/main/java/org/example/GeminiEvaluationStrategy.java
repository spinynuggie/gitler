package org.example;

public class GeminiEvaluationStrategy implements EvaluationStrategy {
    @Override
    public String evaluate(String vraag, String antwoord) {
        return GeminiService.evaluate(vraag, antwoord);
    }
}
