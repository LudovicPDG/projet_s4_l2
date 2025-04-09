package com.iarpg.app.data;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class PlayerCharacter {
    private final int maxHealthPoints;
    private String name;
    private Map<String, Integer> stats;
    private List<Item> inventory;
    private String backstory;
    private int healthPoints;

    private String characterClass;


    public PlayerCharacter(String characterClass, String backstory) {
        this.characterClass = characterClass;
        this.stats = new HashMap<>();
        this.inventory = new ArrayList<>();
        this.backstory = backstory;
        this.maxHealthPoints = 190; // par défaut
        this.healthPoints = this.maxHealthPoints;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public Integer checkStat(String statName) {
        return stats.get(statName);
    }

    public int modifyHealthPoints(Integer amount) {
        this.healthPoints += amount;
        if (this.healthPoints < 0) {
            this.healthPoints = 0; // Empêche les points de vie négatifs
        }

        return this.healthPoints;
    }

//    public void modifyStat(String statName, int amount) {
//        if (stats.containsKey(statName)) {
//            stats.put(statName, stats.get(statName) + amount);
//        } else {
//            stats.put(statName, amount); // Ajoute la statistique si elle n'existe pas encore
//        }
//
//        if (stats.get(statName) < 0) {
//            stats.put(statName, 0);
//        }
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    public void setStats(Map<String, Integer> stats) {
        this.stats = stats;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public String getBackstory() {
        return backstory;
    }

    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }
}