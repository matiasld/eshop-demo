package com.example.eshop.model;

public class User {

    private String email;
    private String name;
    private String profileUrl;
    private String username;

    public User(String email, String name, String profileUrl, String username) {
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.username = username;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
