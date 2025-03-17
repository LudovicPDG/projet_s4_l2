package com.iarpg.backend.backend;

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

@RestController
@RequestMapping("/AIGenerator")
public class AIGenerator {

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private final String GEMINI_IMAGE_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp-image-generation:generateContent";
    private final String API_KEY = "AIzaSyDaSEbCPdENpzmO-0I6PrWAZTnIONSNJek";

    public static void main(String[] args) {
        SpringApplication.run(AIGenerator.class, args);
    }

    @GetMapping("/generateScenario")
    public ResponseEntity<String> generateScenario(@RequestParam String roomTitle, @RequestParam Boolean finalRoom) {
        String prompt = "Generate a RPG scenario for this room. The name of the room is " + roomTitle + (finalRoom ? "it's the final room." : "");
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate scenario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generateRoomDescription")
    public ResponseEntity<String> generateRoomDescription(@RequestParam String roomTitle, @RequestParam Boolean finalRoom) {
        String prompt = "Generate a room description for this room. The name of the room is " + roomTitle + (finalRoom ? "it's the final room." : "");
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);
        String url = GEMINI_API_URL + "?key=" + API_KEY;

        System.out.println(requestBody);

        try {
            String response = restTemplate.postForObject(url, requestBody, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate scenario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/generateImage")
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
    }



}