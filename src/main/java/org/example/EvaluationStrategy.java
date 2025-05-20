package org.example;

public interface EvaluationStrategy {
    /**
     * @param vraag   The question
     * @param antwoord The user's answer
     * @return feedback string, e.g. "GOED: Because …" or "FOUT: Because …"
     */
    String evaluate(String vraag, String antwoord);
}
