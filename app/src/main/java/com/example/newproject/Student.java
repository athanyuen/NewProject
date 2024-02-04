package com.example.newproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Student extends User{
    private int graduatingYear;
    private ArrayList<String> parentUIDs;

    public Student(String uid, String name, String email, String userType, String password, double priceMultiplier, ArrayList<String> ownedVehicles, ArrayList<String> parentUIDs) {
        super(uid, name, email, userType, password, priceMultiplier, ownedVehicles);
        this.parentUIDs = parentUIDs;
    }
}
