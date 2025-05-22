package org.example;

public class AttackStrategy implements HinderingStrategy {
    @Override
    public void hinder(Player player, int strength) {
        int hp = player.hp;
        hp -= strength;
        System.out.println("Monster attacks! Player loses " + strength + " HP.");

        player.hp--;
    }
}


