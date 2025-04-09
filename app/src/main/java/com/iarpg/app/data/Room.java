package com.iarpg.app.data;

import androidx.annotation.NonNull;

public class Room {

    private final String title;
    private final String  description;
    private final Choice choice;
    private final Item item;

    private final int nextHealthPoints;



    public Room(String title, String description, Choice choice, Item item, int nextHealthPoints) {
        this.title = title;
        this.description = description;
        this.choice = choice;
        this.item = item;
        this.nextHealthPoints = nextHealthPoints;
    }

    public int getNextHealthPoints() {
        return nextHealthPoints;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Choice getChoice() { return choice; }

    public Item getItem() { return item; }

    @NonNull
    public String toString() {
        return String.format("title: %s\ndescription: %s", title, description);
    }

    public String getGameString() {
        return String.format("%s\n\nAction 1 : %s\n\nAction 2 : %s", description, choice.getChoice1(), choice.getChoice2());
    }

}
