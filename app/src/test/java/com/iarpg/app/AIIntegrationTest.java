package com.iarpg.app;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertNotEquals;

import com.iarpg.app.data.AIIntegration;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AIIntegrationTest {

    @Test
    public void generateRoomTitlesTest() throws IOException {
        List<String> result = AIIntegration.generateRoomTitles("donjon%20sombre");

        assertEquals("Le résultat doit contenir 30 éléments", 30, result.size());

        System.out.println(result.toString());
    }

    @Test
    public void generateRoomDescriptionTest() throws IOException {
        Map<String, String> result = AIIntegration.generateRoomDescription("Bosquet%20sacre", "Marais%20humides", false, "mage");

        assertEquals("Le résultat doit contenir 9 éléments", 9, result.size());

        System.out.println(result.toString());
    }

    @Test
    public void generateCharacterBackstory() throws IOException, JSONException {
        String result = AIIntegration.generateCharacterBackstory("Guerrier rang maître", "Un puissant guerrier qui adore le fromage", "Philosophique et engagé");

        assertNotEquals("Le résultat ne doit pas être la chaine vide", "" , result);

        System.out.println(result.toString());
    }


}
