package com.example.newproject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Student extends User{
    private int graduatingYear;
    private ArrayList<String> parentUIDs;

    public Student(String email, String password) {
        super(email, password);
    }
}
