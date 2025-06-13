package org.example;

public class Messages {
    public static final String KAMER_KEUZE = "\n🎯 Kies een kamer (of typ 'exit'):";
    public static final String HP_SCORE = "\n📍 HP: %d | Score: %d\n";
    public static final String KEUZE_PROMPT = "Keuze: ";
    public static final String ONGELDIGE_INVOER = "⚠️ Ongeldige invoer. Probeer opnieuw.";
    public static final String KAMER_BESTAAT_NIET = "⚠️ Kamer %d bestaat niet. Probeer opnieuw.";
    public static final String NIET_AANGRENZEND = "❌ Je kunt niet naar kamer %d lopen — niet aangrenzend!";
    public static final String JOKER_PROMPT = "Wil je een joker gebruiken om de %s over te slaan? (y/n): ";
    public static final String GEEN_JOKER_MEER = "❌ Je hebt je joker al gebruikt!";
    public static final String JOKER_GEBRUIKT = "🃏 Joker gebruikt! %s automatisch goed gerekend.";
    public static final String ZWAARD_GEVONDEN = "🗡️ Je vond een ZWAARD in deze kamer!";
    public static final String GAME_OVER = "\n💀 Je hebt geen HP meer. Game over!";

    // Game messages
    public static final String TERUG_NAAR_MENU = "↩️  Terug naar hoofdmenu...";
    public static final String KAMER_VOLTOOID = "✅ Deze kamer heb je al voltooid.";
    public static final String MONSTER_ONTMOETING = "👾 Je staat tegenover: %s!";
    public static final String WAT_WIL_JE_DOEN = "Wat wil je doen?";
    public static final String OPTIE_FIGHT = "1. FIGHT";
    public static final String OPTIE_CHECK = "2. CHECK";
    public static final String MONSTER_VERSLAGEN = "🏆 Je hebt het monster verslagen!";
    public static final String KAMER_SCORE = "✅ Goed! Kamer %d voltooid. +10 score%n";
    public static final String MONSTER_LEEFT = "⚔️ Het monster leeft nog! Je moet nog een vraag beantwoorden.";
    public static final String ONGELDIGE_ACTIE = "⚠️ Ongeldige keuze. Kies '1' voor FIGHT of '2' voor CHECK.";
    public static final String VRAGEN_OP = "❌ Je hebt alle vragen gehad, maar het monster leeft nog! Probeer het opnieuw.";

    // Title screen messages
    public static final String MENU_KEUZE = "Maak een keuze: ";
    public static final String GAME_OPGESLAGEN = "📂 Game opgeslagen!";
    public static final String RESET_BEVESTIGING = "⚠️ Weet je zeker dat je reset wilt? (y/n): ";
    public static final String RESET_VOLTOOID = "🗑️  Save gewist en nieuwe speler gestart met %d HP!";
    public static final String TOT_ZIENS = "👋 Tot ziens!";
    public static final String ONGELDIGE_MENU_KEUZE = "⚠️ Ongeldige keuze. Probeer opnieuw.";
    
    // Controls and Help messages
    public static final String CONTROLS_HEADER = "\n🕹️  Controls:";
    public static final String CONTROLS_KAMER = " - Typ het kamernummer om te spelen";
    public static final String CONTROLS_SAVE = " - Typ 'save' om handmatig op te slaan";
    public static final String CONTROLS_RESET = " - Typ 'reset' om opnieuw te beginnen";
    public static final String CONTROLS_EXIT = " - Typ 'exit' om terug te gaan naar dit menu\n";
    public static final String HELP_HEADER = "\n❓ Help:";
    public static final String HELP_UITLEG = "Beantwoord in elke kamer de vraag zo kort mogelijk.";
    public static final String HELP_AI = "De AI geeft GOED of FOUT met één zin toelichting.\n";

    // Map and Inventory messages
    public static final String MAP_HEADER = "🗺️  Map";
    public static final String INVENTORY_HEADER = "\n🎒 Inventory:";
    public static final String INVENTORY_EMPTY = "  (leeg)";
    public static final String INVENTORY_ITEM = "  - %s: %s";

    // Settings messages
    public static final String SETTINGS_HEADER = "\n--- Settings ---";
    public static final String SETTINGS_DEBUG = "1. Toggle DEBUG (nu %s)";
    public static final String SETTINGS_BACK = "2. Terug";
    public static final String DEBUG_STATUS = "DEBUG staat nu %s.\n";
    public static final String SETTINGS_INVALID = "⚠️ Ongeldige keuze.\n";

    // Hint messages
    public static final String HINT_PROMPT = "❔ Wil je een hint? (y/n): ";
    public static final String NO_HINT = "🔕 Geen hint gekozen.";

    // Monster messages
    public static final String MONSTER_DAMAGE = "🗡️  Je raakt %s voor %d (Monster HP: %d)%n";
    public static final String MONSTER_STATS = "Naam: %s\nHP: %d\nAanvalskracht: %d";

    // New message
    public static final String ASSISTANT_INFO = "💡 Typ 'gebruik assistent' als antwoord om een AI-hint te krijgen.";

    // New message
    public static final String FLEE_SUCCESS = "🏃‍♂️ Je bent succesvol gevlucht! Het monster blijft in deze kamer.";
} 