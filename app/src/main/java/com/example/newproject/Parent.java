package com.example.newproject;

import java.util.ArrayList;

public class Parent  extends User{

    private ArrayList<String> studentUIDs;

    public Parent(String uid, String name, String email, String userType, String password, double priceMultiplier, ArrayList<String> ownedVehicles, ArrayList<String> studentUIDs) {
        super(uid, name, email, userType, password, priceMultiplier, ownedVehicles);
        this.studentUIDs = studentUIDs;
    }
}
