package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Job;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Service
public class GPTService {

    private static final Logger logger = Logger.getLogger(GPTService.class);

    @Value("${ApiKey}")
    private String API_KEY;

    public String analyzeResumeAndMatchJobs(String resumeText, List<Job> jobList) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            logger.error("API Key is not set. Please ensure OPENAI_API_KEY is configured.");
            return "Error: API Key is missing.";
        }

        String gptApiUrl = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", generateMessages(resumeText, jobList));
        requestBody.put("max_tokens", 250);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(gptApiUrl, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Failed to get response from OpenAI API: " + response.getStatusCode());
                return "Error analyzing resume and matching jobs.";
            }
            return parseResponse(response.getBody(), jobList);
        } catch (Exception e) {
            logger.error("Exception occurred while calling OpenAI API: " + e.getMessage(), e);
            return "Error analyzing resume and matching jobs.";
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
        prompt.append("Analyze the following resume and suggest the top 3 jobs that fit the candidate's qualifications. For each suggested job, explain why you believe the candidate would succeed in this position, taking into account their skills, experience, and education.\n")
                .append(resumeText)
                .append("\n\nJobs to consider:\n");

        for (Job job : jobList) {
            prompt.append(String.format("Job Title: %s\nDescription: %s\n\n", job.getTitle(), job.getDescription()));
        }

        prompt.append("Please respond with the 3 job titles that best match the candidate's qualifications and explain why they are a good fit for each.");

        return prompt.toString();
    }

    private String parseResponse(String responseBody, List<Job> jobList) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        logger.info("OpenAI Response: " + jsonResponse.toString());

        String aiResponse = "Error: Unable to parse OpenAI response.";
        if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0) {
            JSONObject firstChoice = jsonResponse.getJSONArray("choices").getJSONObject(0);
            if (firstChoice.has("message")) {
                Object message = firstChoice.get("message");
                if (message instanceof JSONObject) {
                    aiResponse = ((JSONObject) message).getString("content");
                } else if (message instanceof String) {
                    aiResponse = (String) message;
                }
            }
        }

        // Extract top 3 job titles from AI response
        List<String> topJobTitles = extractTopJobTitles(aiResponse);

        StringBuilder responseWithLinks = new StringBuilder(aiResponse);
        responseWithLinks.append("\n\nFor checking these job opportunities, please visit:\n");

        for (Job job : jobList) {
            if (topJobTitles.contains(job.getTitle())) {
                responseWithLinks.append(String.format("<a href=\"/CandidateJobList?jobId=%d\">%s</a><br>", job.getId(), job.getTitle()));
            }
        }


        return responseWithLinks.toString();
    }

    private List<String> extractTopJobTitles(String aiResponse) {
        List<String> topJobTitles = new ArrayList<>();
        String[] lines = aiResponse.split("\n");
        for (String line : lines) {
            if (line.startsWith("1. Job Title:") || line.startsWith("2. Job Title:") || line.startsWith("3. Job Title:")) {
                topJobTitles.add(line.substring(line.indexOf("Job Title:") + 11).trim());
            }
        }
        return topJobTitles;
    }
}
