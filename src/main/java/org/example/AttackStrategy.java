package org.example;

public class AttackStrategy implements HinderingStrategy {
    @Override
    public void hinder(Player player, int strength) {
        player.hp = Math.max(player.hp - strength, 0);
        System.out.println("Het monster valt aan! Je verliest "
                + strength
                + " HP. (Nu heb je nog " + player.hp + " HP)");
    }
}
