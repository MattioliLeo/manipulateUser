package com.github.mattiolileo.keycloak;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.mattiolileo.keycloak.config.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeycloakImpersonationService {
    @Autowired
    private AppConfig appConfig;

    public List<String> listUsers() {
        Client client = ClientBuilder.newClient();
        try {
            String adminToken = getAdminAccessToken(client);

            String usersUrl = String.format("%s/admin/realms/%s/users", appConfig.getAuthUrl(), appConfig.getRealm());
            System.out.println("calling: " + usersUrl);
    
            // Trovare l'ID dell'utente basato sul nome utente
            Response searchResponse = client.target(usersUrl)
                    .queryParam("max", 100) //max 100 results
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + adminToken)
                    .get();
            if (searchResponse.getStatus() != 200) {
                throw new IllegalStateException("Error during list user. Return code: " + searchResponse.getStatus());
            }
    
            // Getting user ID
            Map<String, Object>[] users = searchResponse.readEntity(Map[].class);
    
            List<String> userList = new ArrayList<String>();
            for (Map<String,Object> user : users) {
                userList.add((String) user.get("username"));
                
                System.out.println(
                    String.format("User found: id=%s, username=%s, email=%s", user.get("id"), user.get("username"), user.get("email"))
                );
            }
    
            return userList;
        } finally {
            client.close();
        }
    }

    public String impersonateUser(String username) {
        Client client = ClientBuilder.newClient();
        try {
            // Get Admin token
            String adminToken = getAdminAccessToken(client);

            // Endpoint per ottenere l'utente da impersonare
            String usersUrl = String.format("%s/admin/realms/%s/users", appConfig.getAuthUrl(), appConfig.getRealm());
            System.out.println("calling: " + usersUrl);

            // Trovare l'ID dell'utente basato sul nome utente
            Response searchResponse = client.target(usersUrl)
                    .queryParam("username", username)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + adminToken)
                    .get();

            if (searchResponse.getStatus() != 200) {
                throw new IllegalStateException("Error during user search. Return code: " + searchResponse.getStatus());
            }

            // Getting user ID
            Map<String, Object>[] users = searchResponse.readEntity(Map[].class);
            if (users.length == 0) {
                throw new IllegalStateException("User not found.");
            }
            String userId = (String) users[0].get("id");

            // Let's impersonate the userId
            String impersonateUrl = String.format("%s/realms/%s/protocol/openid-connect/token", appConfig.getAuthUrl(),
                    appConfig.getRealm());
            String requestBody = "client_id=" + appConfig.getTargetClientId() +
                    "&grant_type=urn:ietf:params:oauth:grant-type:token-exchange" +
                    "&client_secret=" + appConfig.getTargetSecret() +
                    "&requested_subject=" + userId;
            Response impersonateResponse = client.target(impersonateUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + adminToken)
                    .post(Entity.entity(requestBody, MediaType.APPLICATION_FORM_URLENCODED));

            if (impersonateResponse.getStatus() != 200) {
                throw new IllegalStateException(
                        "Error during Token-Exchange for impersonation: " + impersonateResponse.getStatus());
            }

            System.out.println(username + " impersonated successfully! Here's the details received:");
            // Print response
            Map<String, Object> impersonateResult = impersonateResponse.readEntity(Map.class);
            for (var entry : impersonateResult.entrySet()) {
                System.out.println("\n" + entry.getKey() + "=" + entry.getValue());
            }

            String accessToken = (String) impersonateResult.get("access_token");


            return accessToken;
        } finally {
            client.close();
        }
    }

    // Helper to get Admin token
    private String getAdminAccessToken(Client client) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", appConfig.getAuthUrl(),
                appConfig.getRealm());

        String requestBody = String.format("client_id=%s&username=%s&password=%s&grant_type=password", 
                appConfig.getAdminClientId(), 
                appConfig.getAdminUsername(), 
                appConfig.getAdminPassword());

        Response response = client.target(tokenUrl)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestBody, MediaType.APPLICATION_FORM_URLENCODED));

        if (response.getStatus() != 200) {
            throw new IllegalStateException("Error during Admin Authentication: " + response.getStatus());
        }

        Map<String, Object> tokenResponse = response.readEntity(Map.class);
        return (String) tokenResponse.get("access_token");
    }
}
