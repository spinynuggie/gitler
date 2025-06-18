package org.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class BattleManualTest {
    static class EvaluationStrategyStub implements EvaluationStrategy {
        @Override
        public String evaluate(String vraag, String answer) {
            return "GOED: Altijd goed!";
        }
    }

    public void testHandleFight_shouldShowFixedHint_whenAssistantIsUsedWithStub() {
        String simulatedInput = "1\ngebruik assistent\nantwoord\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Player player = new Player(10);
        Monster monster = new Monster("TestMonster", 10, 10, new AttackStrategy());
        String fixedHint = "Dit is de vaste stub-hint.";
        HintProvider stubProvider = new StubHintProvider(fixedHint);
        Assistant assistant = new Assistant(stubProvider);
        EvaluationStrategy evaluator = new EvaluationStrategyStub();
        Battle battle = new Battle(scanner, player, monster, null, new GameMap(4), assistant, evaluator);

        battle.handleTurn("Test Vraag?", false);

        System.setOut(originalOut);
        String consoleOutput = outputStream.toString();
        boolean containsHint = consoleOutput.contains(fixedHint);
        boolean containsEdu = consoleOutput.contains("[Educatief Hulpmiddel]");
        boolean containsMotiv = consoleOutput.contains("[Motivatie]");
        if (containsHint && containsEdu && containsMotiv) {
            System.out.println("testHandleFight_shouldShowFixedHint_whenAssistantIsUsedWithStub: GESLAAGD");
        } else {
            System.out.println("testHandleFight_shouldShowFixedHint_whenAssistantIsUsedWithStub: GEFAALD");
        }
    }

    public void testHandleFight_shouldCallAssistantActivate_whenAssistantIsUsedWithMock() {
        String simulatedInput = "1\ngebruik assistent\nantwoord\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        Player player = new Player(10);
        Monster monster = new Monster("TestMonster", 10, 10, new AttackStrategy());
        MockAssistant mockAssistant = new MockAssistant();
        EvaluationStrategy evaluator = new EvaluationStrategyStub();
        Battle battle = new Battle(scanner, player, monster, null, new GameMap(4), mockAssistant, evaluator);
        String testVraag = "Test Vraag?";

        battle.handleTurn(testVraag, false);

        boolean activateCalled = mockAssistant.isActivateCalled();
        boolean questionCorrect = testVraag.equals(mockAssistant.getQuestionPassed());
        if (activateCalled && questionCorrect) {
            System.out.println("testHandleFight_shouldCallAssistantActivate_whenAssistantIsUsedWithMock: GESLAAGD");
        } else {
            System.out.println("testHandleFight_shouldCallAssistantActivate_whenAssistantIsUsedWithMock: GEFAALD");
        }
    }

    public static void main(String[] args) {
        BattleManualTest test = new BattleManualTest();
        test.testHandleFight_shouldShowFixedHint_whenAssistantIsUsedWithStub();
        test.testHandleFight_shouldCallAssistantActivate_whenAssistantIsUsedWithMock();
    }
} 