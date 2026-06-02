package com.br.norris.service;

import com.br.norris.entity.BlingToken;
import com.br.norris.repository.BlingTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class BlingTokenService {

    @Value("${bling.client-id}")
    private String clientId;

    @Value("${bling.client-secret}")
    private String clientSecret;

    private final BlingTokenRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public BlingTokenService(BlingTokenRepository repository) {
        this.repository = repository;
    }

    public void generateToken(String code) {

        String url = "https://www.bling.com.br/Api/v3/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 🔥 ESSENCIAL
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        saveToken(response.getBody());
    }

    public String getValidToken() {
        BlingToken token = repository.findAll().stream().findFirst().orElseThrow();

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken(token);
        }

        return token.getAccessToken();
    }

    private void refreshToken(BlingToken token) {

        String url = "https://www.bling.com.br/Api/v3/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 🔥 ESSENCIAL
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", token.getRefreshToken());

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        saveToken(response.getBody());
    }

    void saveToken(Map body) {
        BlingToken token = repository.findAll().stream().findFirst().orElse(new BlingToken());

        token.setAccessToken((String) body.get("access_token"));
        token.setRefreshToken((String) body.get("refresh_token"));

        Integer expiresIn = (Integer) body.get("expires_in");
        token.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));
        System.out.println("TOKEN GERADO = " + token);
        repository.save(token);
    }
}
