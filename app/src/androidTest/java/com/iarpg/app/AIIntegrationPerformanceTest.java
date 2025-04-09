package com.iarpg.app;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.iarpg.app.data.AIIntegration;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AIIntegrationPerformanceTest {

    @Test
    public void generateRoomTitlesPerformanceTest() throws IOException {
        long startTime = System.currentTimeMillis();

        AIIntegration.generateRoomTitles("donjon%20sombre");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Durée generateRoomTitlesTest : " + duration + " ms");

        assertTrue("La durée doit être inférieure à 5 secondes", duration < 5000);
    }

    @Test
    public void generateRoomDescriptionPerformanceTest() throws IOException {
        long startTime = System.currentTimeMillis();

        AIIntegration.generateRoomDescription("Bosquet%20sacre", "Marais%20humides", false, "mage", 190, "Fantasy");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;


        assertTrue("La durée doit être inférieure à 5 secondes", duration < 5000);
    }

    @Test
    public void generateCharacterBackstoryPerformanceTest() throws IOException, JSONException {
        long startTime = System.currentTimeMillis();

        AIIntegration.generateCharacterBackstory(
                "Guerrier rang maître", "Un puissant guerrier qui adore l'aventure", "Philosophique et engagé");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue("La durée doit être inférieure à 4 secondes", duration < 4000);
    }
}