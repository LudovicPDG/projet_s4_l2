package com.iarpg.backend.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<Map<String, Object>> generateRoomDescription(@RequestParam String roomTitle, @RequestParam String previousRoomTitle, @RequestParam Boolean finalRoom, @RequestParam String characterClass, @RequestParam int playerHealthPoints, @RequestParam String theme) {
        // Construction dynamique du prompt avec les conditions isTrapped et finalRoom
        String prompt = "Remplis le fichier JSON suivant en remplaçant chaque '?' par une valeur selon les critères suivants : \n" +
                "* L'ensemble de l'histoire doit respecter le thème " + theme + "\n." +
                "* roomTitle est égal au paramètre " + roomTitle + "\n" +
                "* roomDescription est une description immersive d'une salle de RPG solo dont le nom est " + roomTitle +". Adresse-toi directement au joueur en le vouvoyant dans cette description. La classe du joueur est " + characterClass + ".\n" +
                "Cette description doit commencer par décrire une transition entre la salle précédente de nom " + previousRoomTitle + " et la nouvelle salle. Si previousRoomTitle = None, cela signifie que c'est la première salle du jeu." +
                "Dans cette description, décris un obstacle (monstre, piège, ou obstacle dangereux)\n" +
                "* choice1 et choice2 sont deux actions possibles pour le joueur. Un seul de ces deux choix a une issue favorable. L'autre choix doit affecter l'état de santé du joueur.\n" +
                "La consequence positive doit aussi décrire l'obtention d'un item, défini par les champs itemName et itemDecription" +
                "* itemName décrit le nom de l'item " +
                "* itemDescription est une description de l'item sur une ligne" +
                "* correctChoice est le numéro du bon choix (1 ou 2)\n" +
                "* consequence1 et consequence2 sont les descriptions des conséquences respectivement aux actions 1 et 2. Les deux conséquences doivent finir par une sortie du joueur de la salle." +
                "* nextHealthPoints est le nombre de points de vie restants au joueur après la conséquence négative. Si le joueur tombe à court de vie (c'est-à-dire nextHealthPoints égale 0) , tu dois décrire sa mort. Le joueur doit perdre moins de 50 points de vie" +
                "Le joueur a actuellement " + playerHealthPoints + "points de vie.\n\n" +
                "{\"roomTitle\": \"?\", \"roomDescription\": \"?\", \"choice1\": \"?\", \"choice2\": \"?\", \"correctChoice\": \"?\", \"consequence1\": \"?\", \"consequence2\": \"?\", \"itemName\" : \"?\", \"itemDescription\" : \"?\", \"nextHealthPoints\" : \"?\"}";

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
            responseMap.put("itemName", json.path("itemName").asText());
            responseMap.put("itemDescription", json.path("itemDescription").asText());
            responseMap.put("nextHealthPoints", json.path("nextHealthPoints").asText());

            return new ResponseEntity<>(responseMap, HttpStatus.OK);

        } catch (Exception e) {
            responseMap.put("error", "Failed to generate room description: " + e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generateRoomTitles")
    public ResponseEntity<List<String>> generateRoomTitles(@RequestBody(required = false) String theme) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        // Prompt
        String prompt = "Génère une liste de 30 titres immersifs pour des salles dans un jeu de rôle. " +
                "Chaque titre doit être court (3 à 5 mots), évocateur et unique. " +
                "Formate-les sous forme d’un tableau JSON de chaînes de caractères." +
                (theme != null && !theme.isEmpty() ? " Le thème général est : " + theme + "." : "");

        // Corps de la requête
        Map<String, Object> body = new HashMap<>();
        body.put("contents", new Object[]{
                new HashMap<String, Object>() {{
                    put("parts", new Object[]{
                            new HashMap<String, Object>() {{
                                put("text", prompt);
                            }}
                    });
                }}
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            String response = restTemplate.postForObject(url, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();

            // Extraction JSON brut d’un tableau
            String jsonArray = text.replaceAll("(?s).*?(\\[.*?\\]).*", "$1");
            List<String> titles = objectMapper.readValue(jsonArray, List.class);

            return ResponseEntity.ok(titles);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Erreur lors de la génération des roomTitles : " + e.getMessage()));
        }
    }



    @GetMapping("/generateBackstory")
    public ResponseEntity<String> generateBackstory(@RequestParam String characterClass, @RequestParam String description, @RequestParam String theme) {
        RestTemplate restTemplate = new RestTemplate();
        String prompt = "Génère une courte histoire immersive d'environ 10 lignes expliquant le passé et les origines d'un personnage de jeu de role basé sur cette description : " + description + ".\n" +
                        "Le thème de l'histoire est " + theme + ".\n" +
                        "Adresse-toi directement au joueur en le vouvoyant comme s'il était ce personnage. Ne finis pas par une question. Termine en disant qu'un long périple attend le joueur.";

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la génération de la backstory : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}