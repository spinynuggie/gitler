package org.example;

import java.util.Map;

public class FunnyHintProvider implements HintProvider {

    private static final Map<String, String> jokes = Map.of(
            "Wat is de rol van de PO?", "😅 De PO is een beetje de DJ van het team: kiest welke nummers (features) er gedraaid worden. Maar hij mixt niet zelf aan de knoppen.",
            "Wat bespreek je tijdens een Daily Scrum?", "🤣 'Ik heb gisteren koffie gezet en vandaag plan ik koffie zetten'.",
            "Wat toon je tijdens de Sprint Review?", "🤡 Geen PowerPoint, geen dromen, alleen het echte spul. Alsof je ouders vragen “wat heb je echt gedaan vandaag?”"
    );

    @Override
    public String getHint(String question) {
        return jokes.getOrDefault(question, "😄 Geen grap beschikbaar... dat is al grappig toch?");
    }
}
