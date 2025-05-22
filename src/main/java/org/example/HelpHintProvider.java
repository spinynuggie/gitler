package org.example;

import java.util.Map;

public class HelpHintProvider implements HintProvider {

    private static final Map<String, String> hints = Map.of(
            "Wat is de rol van de PO?", "💡 Tip: Denk aan wie verantwoordelijk is voor wat er in het product komt — en waarom dat belangrijk is voor de klant.",
            "Wat bespreek je tijdens een Daily Scrum?", "💡 Tip: Vraag jezelf af wat een team moet weten om goed samen te werken op dagelijkse basis. Wat helpt om focus en voortgang te houden?",
            "Wat toon je tijdens de Sprint Review?", "💡 Tip: Denk aan wat je team heeft gebouwd tijdens de sprint en wat de stakeholders willen zien om feedback te geven."
    );

    @Override
    public String getHint(String question) {
        return hints.getOrDefault(question, "💡 Geen specifieke hint beschikbaar voor deze vraag.");
    }
}
