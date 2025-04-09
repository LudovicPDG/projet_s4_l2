package com.iarpg.app.data;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private String description;
    private Map<String, Integer> effectOnStats;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.effectOnStats = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Integer> getEffectOnStats() {
        return effectOnStats;
    }

    public void setEffectOnStats(Map<String, Integer> effectOnStats) {
        this.effectOnStats = effectOnStats;
    }

//    public void applyEffect(PlayerCharacter character) {
//        for (Map.Entry<String, Integer> effect : effectOnStats.entrySet()) {
//            String stat = effect.getKey();
//            Integer value = effect.getValue();
//            character.modifyStat(stat, value);
//        }
//    }
}