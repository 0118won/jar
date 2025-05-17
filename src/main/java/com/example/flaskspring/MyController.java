package com.example.flaskspring;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class MyController {

    @GetMapping("/")
    public String rootMessage(){
        return "서버가 작동 중 입니다";
    }


    @PostMapping("/send")
    public ResponseEntity<String> sendToFlask(@RequestBody Map<String, String> payload) {
        String flaskUrl = "http://localhost:5000/convert";  // Flask 서버 주소

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String result = (String) response.getBody().get("result");
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Flask 서버 응답 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("통신 오류: " + e.getMessage());
        }
    }
}
