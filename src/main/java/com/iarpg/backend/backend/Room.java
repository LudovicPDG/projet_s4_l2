package com.iarpg.backend.backend;

public class Room {
    private final String  description;
    private final String scenario;

    public Room(String description, String scenario) {
        this.description = description;
        this.scenario = scenario;
    }

    public String getDescription() {
        return description;
    }

    public String getScenario() {
        return scenario;
    }
}