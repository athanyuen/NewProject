package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class VehicleInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Vehicle vehicleInfo;
    private ArrayList<Vehicle> vehicleList;
    private Button backButton, refreshButton;
    private FirebaseFirestore db;
    private RecyclerView mRecyclerView;
    private VehicleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);


        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        backButton = findViewById(R.id.back_button_vehicleInfoActivity);
        refreshButton = this.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("vehicle").whereEqualTo("open", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            vehicleList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Vehicle vehicle = document.toObject(Vehicle.class);
                                if (vehicle.getCapacity() > 0) {
                                    vehicleList.add(vehicle);
                                }
                            }

                            mAdapter.setVehicleList(vehicleList);
                        } else {
                            Toast.makeText(VehicleInfoActivity.this, "Refresh Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleInfoActivity.this, MainActivity.class));
            }
        });
        mAdapter = new VehicleAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);


        db.collection("vehicle").whereEqualTo("open", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    vehicleList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Vehicle vehicle = document.toObject(Vehicle.class);
                        if (vehicle.getCapacity() > 0) {
                            vehicleList.add(vehicle);
                        }
                    }
                    mAdapter.setVehicleList(vehicleList);
                } else {
                    Toast.makeText(VehicleInfoActivity.this, "Search Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToAddVehicle(View v) {
        startActivity(new Intent(VehicleInfoActivity.this, AddVehicleActivity.class));
    }


}