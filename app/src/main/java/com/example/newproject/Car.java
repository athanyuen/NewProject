package com.example.newproject;

import java.util.ArrayList;

public class Car  extends Vehicle{
    private int range;

    public Car(String owner, String model, int capacity, String vehicleID, ArrayList<String> ridersUIDs, boolean open, String vehicleType, double basePrice, int range){
        super(owner, model, capacity, vehicleID, ridersUIDs, open, vehicleType, basePrice);
        this.range = range;

    }
}
