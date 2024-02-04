package com.example.newproject;

import java.util.ArrayList;

public class Segway extends Vehicle{
    private int range;
    private int weightCapacity;

    public Segway(String owner, String model, int capacity, String vehicleID, ArrayList<String> ridersUIDs, boolean open, String vehicleType, double basePrice, int range, int weightCapacity){
        super(owner, model, capacity, vehicleID, ridersUIDs, open, vehicleType, basePrice);
        this.range = range;
        this.weightCapacity = weightCapacity;
    }
}
