package com.iarpg.app;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.iarpg.app.data.Choice;
import com.iarpg.app.data.Item;
import com.iarpg.app.data.Room;

public class RoomTest {

    private Room room;
    private Choice choice;
    private Item item;

    @Before
    public void setUp() {
        choice = new Choice("Combattre le dragon", "Trouver une sortie", "Le dragon vous a calciné", "Vous vous êtes enfuis", "2");  // Assurez-vous d'avoir une classe Choice avec un constructeur approprié
        item = new Item("Amulette", "L'amulette est bleue, bleue comme les océans");
        room = new Room("Donjon", "Le donjon du dragon", choice, item, 50);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Donjon", room.getTitle());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Le donjon du dragon", room.getDescription());
    }

    @Test
    public void testGetNextHealthPoints() {
        assertEquals(50, room.getNextHealthPoints());
    }

    @Test
    public void testGetChoice() {
        assertEquals(choice, room.getChoice());
    }

    @Test
    public void testGetItem() {
        assertEquals(item, room.getItem());
    }
}