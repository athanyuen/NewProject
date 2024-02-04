package com.example.newproject;

import java.util.ArrayList;

public class Bicycle  extends Vehicle{
    private String bicycleType;
    private int weight;
    private int weightCapacity;
    public Bicycle(String owner, String model, int capacity, String vehicleID, ArrayList<String> ridersUIDs, boolean open, String vehicleType, double basePrice, String bicycleType, int weight, int weightCapacity){
        super(owner, model, capacity, vehicleID, ridersUIDs, open, vehicleType, basePrice);
        this.bicycleType = bicycleType;
        this.weight = weight;
        this.weightCapacity = weightCapacity;
    }
}
