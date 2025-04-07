package com.iarpg.backend.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> generateRoomDescription(@RequestParam String roomTitle, @RequestParam boolean isTrapped, @RequestParam Boolean finalRoom) {
        String prompt = "Génère une courte description immersive d'une salle de RPG solo. Adresse-toi directement au joueur. Le nom de la salle est "
                + roomTitle + (finalRoom ? "C'est la fin de l'histoire, fais triompher le joueur, et ne finit pas par une question" : "")
                + (isTrapped ? "Décris un obstacle (monstre, piège, ou obstacle dangereux) et deux choix d'action pour le joueur (1 et 2), un mauvais et un bon. Le joueur doit deviner lequel est le bon, ne lui donne pas la solution." +
                                "génère les choix dans un format comme 'Choix :\n1. Choix 1.\n2. Choix 2.' Indique lequel des deux choix est bon en ajoutant '(Bon)' ou '(Mauvais)' après chaque choix. Un choix doit impérativement être en début de ligne." : "");



        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        System.out.println(requestBody);

        Map<String, Object> responseMap = new HashMap<>();

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);


            if (response == null || response.isEmpty()) {
                throw new RuntimeException("La réponse de l'API est vide ou nulle");
            }

            // Convertir la réponse JSON en Map
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode textNode = rootNode.get("candidates").get(0).get("content").get("parts").get(0).get("text");

            String text = textNode.asText();

            System.out.println("generatedText " + text);


            String roomDescription = extractRoomDescription(text);

            List<String> choices = extractChoices(text);

            assert choices.size() == 2;
            System.out.println("choices " + choices);
            String correctChoice = extractCorrectChoice(text);

            //Map<Integer, String> choiceConsequences = getChoiceConsequences(roomDescription, choices.get(0), choices.get(1), correctChoice);

            responseMap.put("roomDescriptionFull", response);
            responseMap.put("roomDescription", roomDescription);
            responseMap.put("choices", choices);
            responseMap.put("correctChoice", correctChoice);


            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } catch (Exception e) {
            responseMap.put("error", "Failed to generate room description: " + e.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractRoomDescription(String response) {
        // On extrait uniquement la partie de la description de la salle.
        return response.split("Choix")[0].trim();  // On suppose que "Choix" est le début des options d'action
    }

    // Méthode pour extraire les choix d'action de la réponse de Gemini
    private List<String> extractChoices(String response) {
        List<String> choices = new ArrayList<>();
        String[] lines = response.split("\n");

        System.out.println("extractChoices, lines " + Arrays.toString(lines));

        for (String line : lines) {
            if (line.startsWith("1.") || line.startsWith("2.")) {
                // On enlève les parenthèses et tout ce qu'elles contiennent
                String clean = line.replaceAll("\\(.*?\\)", "").trim();
                choices.add(clean);
            }
        }

        return choices;
    }

    private String extractCorrectChoice(String response) {
        // --- Format multi-lignes ---
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.startsWith("1.") && line.contains("(Bon)")) {
                return "1";
            } else if (line.startsWith("2.") && line.contains("(Bon)")) {
                return "2";
            }
        }

        // --- Format mono-ligne ---
        int index = response.indexOf("Choix :");
        if (index != -1) {
            String choiceLine = response.substring(index);

            int bonIndex = choiceLine.indexOf("(Bon)");
            int choice1Index = choiceLine.indexOf("1.");
            int choice2Index = choiceLine.indexOf("2.");

            if (bonIndex != -1) {
                if (bonIndex > choice1Index && bonIndex < choice2Index) {
                    return "1";
                } else if (bonIndex > choice2Index) {
                    return "2";
                }
            }
        }

        return "-1"; // Aucun bon choix trouvé
    }

    @GetMapping("/getChoiceConsequences")
    public ResponseEntity<Map<Integer, String>> getChoiceConsequences(@RequestParam String roomDescription, @RequestParam String choice1, @RequestParam String choice2, @RequestParam String goodChoice) {
        String prompt = "Voici la description d'une salle dans un jeu de rôle en solo :\n\n"
                + "\"" + roomDescription + "\"\n\n"
                + "Deux choix s’offrent au joueur :\n"
                + "1. " + choice1 + "\n"
                + "2. " + choice2 + "\n\n"
                + "Décris ce qu’il se passe pour le joueur après qu’il choisit l’un ou l’autre de ces deux choix." +
                " Utilise le format suivant : 1. consequence1\n2. consequence2"
                + (goodChoice.equals("1") ? "La conséquence du choix 1 doit être positive. Celle du choix 2 doit décrire un évènement affectant les points de vie du joueur (sans mentionner le terme point de vie)" : "")
                + (goodChoice.equals("2") ? "La conséquence du choix 2 doit être positive. Celle du choix 1 doit décrire un évènement affectant les points de vie du joueur (sans mentionner le terme point de vie)" : "");

        RestTemplate restTemplate = new RestTemplate();
        String url = GEMINI_API_URL + "?key=" + API_KEY;
        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);

//        try {
//            String response = restTemplate.postForObject(url, requestBody, String.class);
//
//            Map<Integer, String> consequences = new HashMap<>();
//            consequences.put(1, extractConsequence(response, "1\\.", "2\\."));
//            consequences.put(2, extractConsequence(response, "2\\.", null));
//            return consequences;
//
//        } catch (Exception e) {
//            System.err.println("Erreur lors de la génération des conséquences : " + e.getMessage());
//            return Collections.emptyMap();
//        }
        try {
            // Appel API pour générer le contenu
            String response = restTemplate.postForObject(url, requestBody, String.class);
            System.out.println("Response from API: " + response);

            // Initialisation du map pour stocker les conséquences
            Map<Integer, String> consequences = new HashMap<>();

            // Extraction des conséquences pour les choix 1 et 2
            consequences.put(1, extractConsequence(response, "1\\.", "2\\."));
            consequences.put(2, extractConsequence(response, "2\\.", null));

            // Retourner les conséquences dans une ResponseEntity avec statut OK
            return new ResponseEntity<>(consequences, HttpStatus.OK);

        } catch (Exception e) {
            // En cas d'erreur, affichage du message et renvoi d'une réponse vide
            System.err.println("Erreur lors de la génération des conséquences : " + e.getMessage());
            return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractConsequence(String text, String startPattern, String endPattern) {
        try {
            int startIndex = text.indexOf(startPattern);
            if (startIndex == -1) {
                return ""; // Si le début du choix n'est pas trouvé
            }

            // Décaler le début pour passer le numéro du choix
            startIndex += startPattern.length();

            // Si on a un "endPattern", on cherche où il commence
            int endIndex = -1;
            if (endPattern != null) {
                endIndex = text.indexOf(endPattern, startIndex);
                if (endIndex == -1) {
                    endIndex = text.length(); // Si le "endPattern" n'est pas trouvé, on prend tout jusqu'à la fin du texte
                }
            } else {
                endIndex = text.length(); // Si pas de "endPattern", on prend jusqu'à la fin du texte
            }

            // Extraire la portion de texte entre startIndex et endIndex
            String consequence = text.substring(startIndex, endIndex).trim();

            // Enlever les retours à la ligne inutiles et autres espaces superflus
            return consequence.replaceAll("\\n+", " ").trim();

        } catch (Exception e) {
            System.err.println("Erreur dans extractConsequence : " + e.getMessage());
            return "";
        }
    }




    /*@GetMapping("/generateImage")
    public ResponseEntity<String> generateImage(@RequestParam String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_IMAGE_API_URL + "?key=" + API_KEY;

        File file = new File("/path/to/your/stocked/image.jpg");

        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            // Set the content type to match your image type (e.g., IMAGE_JPEG, IMAGE_PNG)
            headers.setContentType(MediaType.IMAGE_JPEG);
            // Add a custom header to return the image's absolute file path
            headers.add("X-Image-Location", file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());
            return new ResponseEntity<>(file.getAbsolutePath(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

   /* private String determineCorrectChoice(List<String> choices) {
        // Vous pouvez définir votre propre logique ici pour déterminer quel choix est correct.
        // Par exemple, vous pouvez utiliser un prompt comme suit pour demander à Gemini de choisir :
        String choicePrompt = "Voici deux choix d'action pour la salle décrite ci-dessus :\n"
                + "1. " + choices.get(0) + "\n"
                + "2. " + choices.get(1) + "\n"
                + "Fais appel au bon sens pour déterminer lequel est le bon. Ta réponse doit contenir exactement 1 ou 2";

        String correctChoiceRequest = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", choicePrompt);

        RestTemplate restTemplate = new RestTemplate();
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        System.out.println("Requête " + correctChoiceRequest);

        // Appel à Gemini pour déterminer le bon choix
        try {
            String correctChoiceResponse = restTemplate.postForObject(url, correctChoiceRequest, String.class);

            System.out.println("Correct choice : " + correctChoiceResponse);
            // Extraire le bon choix de la réponse
            if (correctChoiceResponse.contains("1")) {
                return "1";  // Si Gemini répond "1", alors le bon choix est le premier
            } else if (correctChoiceResponse.contains("2")) {
                return "2";  // Si Gemini répond "2", alors le bon choix est le second
            } else {
                return "Inconnu";  // Si Gemini n'a pas donné une réponse claire
            }
        } catch (Exception e) {
            // Si une erreur se produit lors de la demande de choix
            System.out.println("default correct choice");
            return "1";
        }
    }*/

    /*    @GetMapping("/generateScenario")
    public ResponseEntity<String> generateScenario(@RequestParam String roomTitle, @RequestParam Boolean finalRoom) {
        String prompt = "Génére un scénario de RPG en solo pour une salle. Le nom de la salle est " + roomTitle + (finalRoom ? "C'est la salle finale, marquant la fin de l'histoire" : "");
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate scenario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

/*    @GetMapping("/generateRoomActions")
    public ResponseEntity<String> generateScenario(@RequestParam String roomDescription) {
        String prompt = "Dans un RPG solo, génère deux actions possibles pour un joueur dans la salle ayant pour description : " + roomDescription;
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate scenario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

}