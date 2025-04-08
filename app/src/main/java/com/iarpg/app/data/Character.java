package com.iarpg.app.data;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Character {
    private String name;
    private Map<String, Integer> characterStats;
    private List<Item> inventory;
    private String backstory;
    private String image;
    private Integer healthPoints;


    public Character(String name, Map<String, Integer> stats, String backstory, String image) {
        this.name = name;
        this.characterStats = new HashMap<>(stats);
        this.inventory = new ArrayList<>();
        this.backstory = backstory;
        this.image = image;
        this.healthPoints = 100; // par défaut
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public Integer checkStat(String statName) {
        return characterStats.getOrDefault(statName, 0);
    }

    public void modifyHealthPoints(Integer amount) {
        this.healthPoints += amount;
        if (this.healthPoints < 0) {
            this.healthPoints = 0; // Empêche les points de vie négatifs
        }
    }

    public void modifyStat(String statName, Integer amount) {
        if (characterStats.containsKey(statName)) {
            characterStats.put(statName, characterStats.get(statName) + amount);
        } else {
            characterStats.put(statName, amount); // Ajoute la statistique si elle n'existe pas encore
        }

        if (characterStats.get(statName) < 0) {
            characterStats.put(statName, 0);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCharacterStats() {
        return characterStats;
    }

    public void setCharacterStats(Map<String, Integer> characterStats) {
        this.characterStats = characterStats;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(Integer healthPoints) {
        this.healthPoints = healthPoints;
    }
}