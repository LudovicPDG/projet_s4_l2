package com.iarpg.backend.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/AIGenerator")
public class AIGenerator {

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private final String GEMINI_IMAGE_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp-image-generation:generateContent";
    private final String API_KEY = "AIzaSyDaSEbCPdENpzmO-0I6PrWAZTnIONSNJek";

    public static void main(String[] args) {
        SpringApplication.run(AIGenerator.class, args);
    }


    @GetMapping("/generateRoomDescription")
    public ResponseEntity<Map<String, Object>> generateRoomDescription(@RequestParam String roomTitle, @RequestParam Boolean finalRoom, @RequestParam char inspirationLetter) {
        // Construction dynamique du prompt avec les conditions isTrapped et finalRoom
        String prompt = "Remplis le fichier JSON suivant en remplaçant chaque '?' par une valeur selon les critères suivants : \n" +
                "* roomTitle est le titre de la salle en un seul mot, choisis celui-ci dans un univers de fantasy. roomTitle doit être un lieu surprenant, peu commun dans les jeux fantasy classiques, mais toujours crédible. roomTitle doit être un nom commun commençant par la lettre" + inspirationLetter + "\n" +
                "* roomDescription est une description immersive d'une salle de RPG solo dont le nom est roomTitle. Adresse-toi directement au joueur dans cette description." +
                "Dans cette description, décris un obstacle (monstre, piège, ou obstacle dangereux)\n" +
                "* choice1 et choice2 sont deux actions possibles pour le joueur. Un seul de ces deux choix a une issue favorable. L'autre est un choix pénalisant, mais non mortel.\n" +
                "* correctChoice est le numéro du bon choix (1 ou 2)\n" +
                "* consequence1 et consequence2 sont les descriptions des conséquences respectivement aux actions 1 et 2. Les deux conséquences doivent finir par une sortie du joueur de la salle.\n\n" +
                "{\"roomTitle\": \"?\", \"roomDescription\": \"?\", \"choice1\": \"?\", \"choice2\": \"?\", \"correctChoice\": \"?\", \"consequence1\": \"?\", \"consequence2\": \"?\"}";

        // Vérification des paramètres spécifiques
        if (finalRoom) {
            prompt += "\nC'est la fin de l'histoire, fais triompher le joueur, et ne finis pas par une question.";
        }

        // Création de la requête
        RestTemplate restTemplate = new RestTemplate();
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        // Le corps de la requête sera un JSON
        Map<String, Object> body = new HashMap<>();
        String finalPrompt = prompt;
        body.put("contents", new Object[] {
                new HashMap<String, Object>() {{
                    put("parts", new Object[] {
                            new HashMap<String, Object>() {{
                                put("text", finalPrompt);
                            }}
                    });
                }}
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Map pour stocker la réponse
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Envoi de la requête POST à l'API
            String response = restTemplate.postForObject(url, entity, String.class);

            System.out.println("response: " + response);

            // Vérification de la réponse
            if (response == null || response.isEmpty()) {
                throw new RuntimeException("La réponse de l'API est vide ou nulle");
            }

            // Traitement de la réponse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            String textResponse = rootNode.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();

            String jsonContent = textResponse.replaceAll("(?s)```json\\s*(\\{.*?\\})\\s*```", "$1");

            JsonNode json = objectMapper.readTree(jsonContent);


            // Remplissage de la Map de réponse
            responseMap.put("roomTitle", json.path("roomTitle").asText());
            responseMap.put("roomDescription", json.path("roomDescription").asText());
            responseMap.put("choice1", json.path("choice1").asText());
            responseMap.put("choice2", json.path("choice2").asText());
            responseMap.put("correctChoice", json.path("correctChoice").asText());
            responseMap.put("consequence1", json.path("consequence1").asText());
            responseMap.put("consequence2", json.path("consequence2").asText());

            return new ResponseEntity<>(responseMap, HttpStatus.OK);

        } catch (Exception e) {
            responseMap.put("error", "Failed to generate room description: " + e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/generateBackstory")
    public ResponseEntity<String> generateBackstory(@RequestParam String description) {
        RestTemplate restTemplate = new RestTemplate();
        String prompt = "Génère une courte histoire immersive expliquant le passé et les origines d'un personnage de jeu de role basé sur cette description : " + description;

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la génération de la backstory : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generatePortrait")
    public ResponseEntity<String> generatePortrait(@RequestParam String description, @RequestParam String characterClass) {
        RestTemplate restTemplate = new RestTemplate();
        String prompt = "Génère un portrait pour un personnage de jeu de rôle de classe " + characterClass + " avec cette description : " + description;

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_IMAGE_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la génération du portrait : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}