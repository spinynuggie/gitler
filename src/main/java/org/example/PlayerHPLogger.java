package org.example;

public class PlayerHPLogger implements PlayerObserver {
    @Override
    public void update(Player player) {
        System.out.println("[HP LOGGER] Speler HP is nu: " + player.getHp());
    }
} 