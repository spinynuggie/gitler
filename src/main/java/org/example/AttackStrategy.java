package org.example;

public class AttackStrategy implements HinderingStrategy {
    @Override
    public void hinder(Player player, int strength) {
        // subtract exactly `strength` (never below 0)
        player.hp = Math.max(player.hp - strength, 0);
        System.out.println("Monster attacks! Player loses "
                + strength
                + " HP. (Now at " + player.hp + " HP)");
    }
}
