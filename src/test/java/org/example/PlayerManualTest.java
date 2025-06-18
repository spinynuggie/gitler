package org.example;

public class PlayerManualTest {
    static class HintProviderStub {
        public String getHint() {
            return "This is a fixed hint";
        }
    }

    static class ScoreManagerMock {
        private boolean addScoreCalled = false;
        private int score;

        public void addScore(int score) {
            this.score = score;
            addScoreCalled = true;

        }

        public boolean isAddScoreCalled() {
            return addScoreCalled;
        }
    }

    public void setUp() {
        new Player(100);
    }

    public void testHintProviderWithStub() {
        HintProviderStub hintProvider = new HintProviderStub();
        String hint = hintProvider.getHint();
        if ("This is a fixed hint".equals(hint)) {
            System.out.println("testHintProviderWithStub: GESLAAGD");
        } else {
            System.out.println("testHintProviderWithStub: GEFAALD");
        }
    }

    public void testScoreManagerWithMock() {
        ScoreManagerMock scoreManager = new ScoreManagerMock();
        scoreManager.addScore(10);
        if (scoreManager.isAddScoreCalled()) {
            System.out.println("testScoreManagerWithMock: GESLAAGD");
        } else {
            System.out.println("testScoreManagerWithMock: GEFAALD");
        }
    }

    public static void main(String[] args) {
        PlayerManualTest test = new PlayerManualTest();
        test.setUp();
        test.testHintProviderWithStub();
        test.testScoreManagerWithMock();
    }
} 