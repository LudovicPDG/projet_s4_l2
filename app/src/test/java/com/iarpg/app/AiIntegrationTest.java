package com.iarpg.app;

import static junit.framework.TestCase.assertEquals;

import com.iarpg.app.data.AIIntegration;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AiIntegrationTest {

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
}
