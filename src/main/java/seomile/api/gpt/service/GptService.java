package seomile.api.gpt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GptService {


    @Value("${openai.api.key}")
    private String openaiApiKey;
    public String callGptApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.5);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        }
        return "Error: " + response.getStatusCode();
    }


}