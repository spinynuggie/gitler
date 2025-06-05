package org.example;

import java.util.*;

public final class Questions {
    private Questions() {}

    private static final List<Question> LIST = List.of(
            new Question("Sprint Planning",  "Wat is de rol van de PO?"),
            new Question("Daily Scrum",       "Wat bespreek je tijdens een Daily Scrum?"),
            new Question("Sprint Review",     "Wat toon je tijdens de Sprint Review?")
    );

    public static List<Question> all()     { return LIST; }
    public static Question get(int i)      { return LIST.get(i); }
    public static int size()               { return LIST.size(); }
    public static Question random()        { return LIST.get(new Random().nextInt(LIST.size())); }
}

class Question {
    private final String name, text;
    public Question(String name, String text) {
        this.name = name; this.text = text;
    }
    public String getName() { return name; }
    public String getText() { return text; }
}
