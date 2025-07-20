package com.bitlab.mainserviceclean.service;

import com.bitlab.mainserviceclean.dto.auth.AuthRequest;
import com.bitlab.mainserviceclean.dto.auth.AuthResponse;
import com.bitlab.mainserviceclean.dto.auth.RegisterRequest;
import com.bitlab.mainserviceclean.dto.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.base-url}")
    private String baseUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "admin-cli");
        params.put("username", "admin");
        params.put("password", "admin123");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "/realms/master/protocol/openid-connect/token",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new RuntimeException("Не удалось получить access_token от Keycloak");
        }

        return (String) body.get("access_token");
    }

    public AuthResponse authenticate(AuthRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", request.getUsername());
        params.put("password", request.getPassword());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                baseUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                entity,
                AuthResponse.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        return response.getBody();
    }

    public void createUser(RegisterRequest dto) {
        String token = getAdminToken();

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", dto.getUsername());
        userPayload.put("enabled", true);
        userPayload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", dto.getPassword(),
                "temporary", false
        )));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users",
                HttpMethod.POST,
                new HttpEntity<>(userPayload, headers),
                String.class
        );

        ResponseEntity<List<Map<String, Object>>> userSearch = restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users?username=" + dto.getUsername(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> users = userSearch.getBody();
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Пользователь не найден после создания");
        }

        String userId = (String) users.get(0).get("id");

        Map<String, Object> role = Map.of(
                "name", dto.getRole(),
                "clientRole", false,
                "composite", false
        );

        restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                HttpMethod.POST,
                new HttpEntity<>(List.of(role), headers),
                String.class
        );
    }

    public void updateCurrentUser(String token, UpdateUserRequest dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        ResponseEntity<Map<String, Object>> profileResponse = restTemplate.exchange(
                baseUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> profile = profileResponse.getBody();
        if (profile == null || !profile.containsKey("sub")) {
            throw new RuntimeException("Не удалось получить ID пользователя");
        }

        String userId = (String) profile.get("sub");

        Map<String, Object> userPayload = new HashMap<>();
        if (dto.getFirstName() != null) userPayload.put("firstName", dto.getFirstName());
        if (dto.getLastName() != null) userPayload.put("lastName", dto.getLastName());
        if (dto.getEmail() != null) userPayload.put("email", dto.getEmail());

        restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users/" + userId,
                HttpMethod.PUT,
                new HttpEntity<>(userPayload, headers),
                Void.class
        );

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            Map<String, Object> passwordPayload = Map.of(
                    "type", "password",
                    "value", dto.getPassword(),
                    "temporary", false
            );

            restTemplate.exchange(
                    baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password",
                    HttpMethod.PUT,
                    new HttpEntity<>(List.of(passwordPayload), headers),
                    Void.class
            );
        }
    }

    public void updateUserRole(String username, String newRole) {
        String token = getAdminToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        ResponseEntity<List<Map<String, Object>>> userSearch = restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users?username=" + username,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> users = userSearch.getBody();
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }

        String userId = (String) users.get(0).get("id");

        ResponseEntity<List<Map<String, Object>>> currentRoles = restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                HttpMethod.DELETE,
                new HttpEntity<>(currentRoles.getBody(), headers),
                Void.class
        );

        Map<String, Object> newRolePayload = Map.of(
                "name", newRole,
                "clientRole", false,
                "composite", false
        );

        restTemplate.exchange(
                baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                HttpMethod.POST,
                new HttpEntity<>(List.of(newRolePayload), headers),
                Void.class
        );
    }
}






