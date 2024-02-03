package com.example.newproject;

import java.util.ArrayList;

public class User {
    private String uid;
    private String name;
    private String email;
    private String userType;
    private String password;
    private double priceMultiplier;
    private ArrayList<String> ownedVehicles;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
