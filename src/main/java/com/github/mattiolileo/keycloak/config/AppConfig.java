package com.github.mattiolileo.keycloak.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

public class AppConfig {

    @Value("${manipulate-user.auth-url}")
    private String authUrl;
    
    @Value("${manipulate-user.realm}")
    private String realm;
    
    @Value("${manipulate-user.admin-client-id}")
    private String adminClientId;
    
    @Value("${manipulate-user.admin-username}")
    private String adminUsername;
    
    @Value("${manipulate-user.admin-password}")
    private String adminPassword;
    
    @Value("${manipulate-user.target-client-id}")
    private String targetClientId;
    
    @Value("${manipulate-user.target-secret}")
    private String targetSecret;

    @PostConstruct
    private void logInitValues(){
        System.out.println("AppConfig!");
        System.out.println(String.format("Configuration loaded: %s [" +
            "authUrl=%s, " +
            "adminClientId=%s, " +
            "adminUsername=%s, " +
            "targetClientId=%s" +
            "]", this.getClass().getName(), authUrl, adminClientId, adminUsername, targetClientId));
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getAdminClientId() {
        return adminClientId;
    }

    public void setAdminClientId(String adminClientId) {
        this.adminClientId = adminClientId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getTargetClientId() {
        return targetClientId;
    }

    public void setTargetClientId(String targetClientId) {
        this.targetClientId = targetClientId;
    }

    public String getTargetSecret() {
        return targetSecret;
    }

    public void setTargetSecret(String targetSecret) {
        this.targetSecret = targetSecret;
    }
}
