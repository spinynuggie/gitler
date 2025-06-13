package org.example;

import java.io.Serializable;

public interface EvaluationStrategy extends Serializable {
    String evaluate(String vraag, String antwoord);
}
