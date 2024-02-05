package com.example.newproject;

import java.util.ArrayList;

public class Alumni extends User{
    private int graduateYear;

    public Alumni(String uid, String name, String email, String userType, String password, double priceMultiplier, ArrayList<String> ownedVehicles, int graduateYear) {
        super(uid, name, email, userType, password, priceMultiplier, ownedVehicles);
        this.graduateYear = graduateYear;
    }

    public int getGraduateYear() {
        return graduateYear;
    }

    public void setGraduateYear(int graduateYear) {
        this.graduateYear = graduateYear;
    }
}
