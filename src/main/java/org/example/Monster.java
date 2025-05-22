package org.example;

public class Monster   {
    private String name;
    private int strength;;
    private int health;

    public Monster(String name, int strength, int health) {
        this.name = name;
        this.strength = strength;
        this.health = health;
    }

    public void hinder(Player player) {
        System.out.println(name + " hindert de speler met kracht: " + strength);
        int hp = player.hp;
        hp -= strength;
    }
}

