package com.iarpg.app.data;


import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIIntegration {
    /* Cette classe permet de faire des appels à l'api d'ia générative */

    public static List<String> generateRoomTitles(String theme) throws IOException {
        List<String> result = new ArrayList<>();

        // Construire l'URL avec le thème
        URL url = new URL("http://localhost:8080/AIGenerator/generateRoomTitles?theme=" + theme);

        // Ouvrir la connexion HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Obtenir le code de réponse HTTP
        int status = connection.getResponseCode();

        // Lire la réponse
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        // Ajouter chaque ligne à la réponse (si c'est une réponse ligne par ligne)
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
            // Si chaque titre de salle est séparé par une virgule (par exemple)
            String[] titles = inputLine.split(",");
            for (String title : titles) {
                result.add(title.trim()); // Ajouter chaque titre à la liste
            }
        }
        in.close();
        connection.disconnect();

        // Afficher la réponse reçue (pour débogage)
        System.out.println("Statut : " + status);
        System.out.println("Réponse : " + content.toString());

        return result;
    }

    public static Map<String, String> generateRoomDescription(String roomTitle, String previousRoomTitle, boolean finalRoom, String characterClass) throws IOException {
        Map<String, String> result = new HashMap<>();

        URL url = new URL(String.format("http://localhost:8080/AIGenerator/" +
                "generateRoomDescription?roomTitle=%s&previousRoomTitle=%s&finalRoom=%s&characterClass=%s",
                roomTitle, previousRoomTitle, finalRoom, characterClass));

        // Faire la requête
        // Ouvrir la connexion HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Lire la réponse du serveur
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        // Lire la réponse ligne par ligne
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        // Afficher la réponse brute
        String response = content.toString();
        System.out.println("Réponse brute : " + response);

        // Nettoyer la réponse pour enlever les caractères inutiles (accolades et guillemets)
        response = response.substring(1, response.length() - 1).trim(); // Retirer les accolades initiales et finales

        // Diviser la réponse en paires clé-valeur, en prenant en compte les guillemets dans les valeurs
        String[] keyValuePairs = response.split("(?<=\")\\s*,\\s*(?=\")"); // Séparation correcte des paires clé-valeur

        // Ajouter les paires clé-valeur à la Map
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":", 2); // Limiter à 2 pour éviter des erreurs
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", ""); // Enlever les guillemets autour de la clé
                String value = keyValue[1].trim().replaceAll("\"", ""); // Enlever les guillemets autour de la valeur
                result.put(key, value);
            }
        }

        // Afficher le résultat
        System.out.println("Le résultat est : " + result);

        return result;
    }

}
