package com.iarpg.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.iarpg.app.data.Item;
import com.iarpg.app.data.PlayerCharacter;

public class CharacterTest {
    private PlayerCharacter character;

    @Before
    public void setUp() {
        character = new PlayerCharacter("Test Character","This is a backstory.");
    }

    @Test
    public void testInitialHealthPoints() {
        assertEquals("Les HP initiales doivent être au max.", character.getMaxHealthPoints(), character.getHealthPoints());
    }

    @Test
    public void testModifyHealthPointsPositive() {
        character.modifyHealthPoints(20);
        assertEquals("Les HP doivent augmenter correctement.", character.getMaxHealthPoints() + 20, character.getHealthPoints());
    }

    @Test
    public void testModifyHealthPointsNegative() {
        character.modifyHealthPoints(-50);
        assertEquals("Les HP doivent diminuer correctement.", character.getMaxHealthPoints() - 50, character.getHealthPoints());
    }

    @Test
    public void testModifyHealthPointsCannotGoBelowZero() {
        character.modifyHealthPoints(-150);
        assertTrue("Les HP ne peuvent pas être inférieures à 0.", character.getHealthPoints() > 0);
    }

    @Test
    public void testGetAndSetName() {
        character.setName("New Name");
        assertEquals("Le nom du personnage doit être modifiable.", "New Name", character.getName());
    }

    @Test
    public void testGetAndSetBackstory() {
        String newBackstory = "This is a new backstory.";
        character.setBackstory(newBackstory);
        assertEquals("La backstory doit être modifiable.", newBackstory, character.getBackstory());
    }

    @Test
    public void testInventoryManagement() {
        Item sword = new Item("Sword", "A sharp blade.");
        Item shield = new Item("Shield", "A sturdy shield.");

        // Ajouter des objets
        character.addItem(sword);
        character.addItem(shield);

        assertEquals("L'inventaire doit contenir deux objets.", 2, character.getInventory().size());

        // Retirer un objet
        character.removeItem(sword);
        assertEquals("L'inventaire doit contenir un seul objet après suppression.", 1, character.getInventory().size());
        assertEquals("L'objet restant doit être le bouclier.", shield, character.getInventory().get(0));
    }
}