package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Job;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GPTService {

    private static final Logger logger = Logger.getLogger(GPTService.class);

    // Hard-coded API key
    private final String API_KEY = "sk-_Ua3GA0KAB-mfh1FrCu495odipU_D4EYxzoXpba_MNT3BlbkFJ6IcybBmeBjrkO0SzG3Jab25Gd_I0gT0X1Y8S23DQ4A"; // Insert  API key here

    public String analyzeResumeAndMatchJob(String resumeText, List<Job> jobList) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            logger.error("API Key is not set. Please ensure OPENAI_API_KEY is configured.");
            return "Error: API Key is missing.";
        }

        String gptApiUrl = "https://api.openai.com/v1/chat/completions";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // Build JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo"); // Adjust model as needed
        requestBody.put("messages", generateMessages(resumeText, jobList)); // Update to use messages
        requestBody.put("max_tokens", 150);

        // Create HttpEntity with headers and request body
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // Make the POST request using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(gptApiUrl, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Failed to get response from OpenAI API: " + response.getStatusCode());
                return "Error analyzing resume and matching job.";
            }
            return parseResponse(response.getBody());
        } catch (Exception e) {
            logger.error("Exception occurred while calling OpenAI API: " + e.getMessage(), e);
            return "Error analyzing resume and matching job.";
        }
    }

    private List<JSONObject> generateMessages(String resumeText, List<Job> jobList) {
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", generatePrompt(resumeText, jobList));

        return List.of(userMessage);
    }

    private String generatePrompt(String resumeText, List<Job> jobList) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following resume and suggest the most fitting job:\n")
                .append(resumeText)
                .append("\n\nJobs to consider:\n");

        for (Job job : jobList) {
            prompt.append(String.format("Job Title: %s\nDescription: %s\n\n", job.getTitle(), job.getDescription()));
        }

        prompt.append("Please respond with the job title that best matches the candidate's qualifications.");

        return prompt.toString();
    }

    private String parseResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        logger.info("OpenAI Response: " + jsonResponse.toString());

        if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0) {
            JSONObject firstChoice = jsonResponse.getJSONArray("choices").getJSONObject(0);
            if (firstChoice.has("message")) {
                Object message = firstChoice.get("message");
                if (message instanceof JSONObject) {
                    return ((JSONObject) message).getString("content");
                } else if (message instanceof String) {
                    return (String) message;
                }
            }
        }

        logger.error("Unexpected response format: " + jsonResponse.toString());
        return "Error: Unable to parse OpenAI response.";
    }



}
