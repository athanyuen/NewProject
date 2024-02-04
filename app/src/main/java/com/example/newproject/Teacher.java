package com.example.newproject;

import java.util.ArrayList;

public class Teacher extends User{
    private String inSchoolTitle;

    public Teacher(String uid, String name, String email, String userType, String password, double priceMultiplier, ArrayList<String> ownedVehicles, String inSchoolTitle) {
        super(uid, name, email, userType, password, priceMultiplier, ownedVehicles);
        this.inSchoolTitle = inSchoolTitle;
    }
}
