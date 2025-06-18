package org.example;

public class PlayerManualBoundaryTest {
    public void testTakeDamage_BoundaryValues() {
        // Test case 1: Schade toebrengen aan een speler met volle HP
        Player player1 = new Player(100);
        player1.takeDamage(10);
        if (player1.getHp() == 90) {
            System.out.println("Test 1: GESLAAGD");
        } else {
            System.out.println("Test 1: GEFAALD (verwacht 90, kreeg " + player1.getHp() + ")");
        }

        // Test case 2: Schade toebrengen waardoor de HP precies 0 wordt
        Player player2 = new Player(10);
        player2.takeDamage(10);
        if (player2.getHp() == 0) {
            System.out.println("Test 2: GESLAAGD");
        } else {
            System.out.println("Test 2: GEFAALD (verwacht 0, kreeg " + player2.getHp() + ")");
        }

        // Test case 3: Meer schade toebrengen dan de speler HP heeft
        Player player3 = new Player(5);
        player3.takeDamage(10);
        if (player3.getHp() == 0) {
            System.out.println("Test 3: GESLAAGD");
        } else {
            System.out.println("Test 3: GEFAALD (verwacht 0, kreeg " + player3.getHp() + ")");
        }

        // Test case 4: 0 schade toebrengen
        Player player4 = new Player(50);
        player4.takeDamage(0);
        if (player4.getHp() == 50) {
            System.out.println("Test 4: GESLAAGD");
        } else {
            System.out.println("Test 4: GEFAALD (verwacht 50, kreeg " + player4.getHp() + ")");
        }
    }

    public static void main(String[] args) {
        PlayerManualBoundaryTest test = new PlayerManualBoundaryTest();
        test.testTakeDamage_BoundaryValues();
    }
} 