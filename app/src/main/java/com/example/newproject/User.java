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

    public User(String uid, String name, String email, String userType, String password, double priceMultiplier, ArrayList<String> ownedVehicles){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.password = password;
        this.priceMultiplier = priceMultiplier;
        this.ownedVehicles = ownedVehicles;
    }
    public User() {
    }
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public ArrayList<String> getOwnedVehicles() {
        return ownedVehicles;
    }

    public void setOwnedVehicles(ArrayList<String> ownedVehicles) {
        this.ownedVehicles = ownedVehicles;
    }
}
