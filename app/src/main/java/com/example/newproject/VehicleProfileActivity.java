package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VehicleProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private Button backButton, bookRideButton;
    private TextView vehicleTypeTextView, ownerTextView, capacityTextView, modelTextView, priceTextView, vehicleIDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        Intent intent = getIntent();
        String selectedVehicle = intent.getStringExtra("selectedVehicle");

        initializeViews();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        bookRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("vehicle").whereEqualTo("vehicleID", selectedVehicle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Vehicle bookedVehicle = document.toObject(Vehicle.class);

                                int newCapacity = bookedVehicle.getCapacity() - 1;

                                if (newCapacity >= 0) {
                                    document.getReference().update("capacity", newCapacity).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            bookedVehicle.setCapacity(newCapacity);
                                            capacityTextView.setText("Capacity: " + newCapacity);
                                            addBookedVehicleToUser(bookedVehicle);
                                        } else {
                                            Toast.makeText(VehicleProfileActivity.this, "Failed to book ride", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(VehicleProfileActivity.this, "No available capacity for this vehicle", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        } else {
                            Toast.makeText(VehicleProfileActivity.this, "Failed to load vehicle data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loadVehicleData(selectedVehicle);
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button_vehicleProfileActivity);
        vehicleTypeTextView = findViewById(R.id.vehicle_type);
        ownerTextView = findViewById(R.id.owner);
        capacityTextView = findViewById(R.id.capacity);
        modelTextView = findViewById(R.id.model);
        priceTextView = findViewById(R.id.price);
        vehicleIDTextView = findViewById(R.id.vehicleID);
        bookRideButton = findViewById(R.id.book_ride);

        backButton.setOnClickListener(v -> finish());
    }

    private void loadVehicleData(String selectedVehicle) {
        db.collection("vehicle").whereEqualTo("vehicleID", selectedVehicle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Vehicle bookedVehicle = document.toObject(Vehicle.class);
                        updateUIWithVehicleData(bookedVehicle);
                        loadUserData(bookedVehicle);
                        break;
                    }
                } else {
                    Toast.makeText(VehicleProfileActivity.this, "Failed to load vehicle data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUserData(Vehicle bookedVehicle) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userType = document.getString("user type");
                            updatePriceBasedOnUserType(bookedVehicle, userType);
                        }
                    } else {
                        Toast.makeText(VehicleProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateUIWithVehicleData(Vehicle bookedVehicle) {
        vehicleTypeTextView.setText("Vehicle Type: " + bookedVehicle.getVehicleType());
        ownerTextView.setText("Owner: " + bookedVehicle.getOwner());
        capacityTextView.setText("Capacity: " + bookedVehicle.getCapacity());
        modelTextView.setText("Model: " + bookedVehicle.getModel());
        vehicleIDTextView.setText("Vehicle ID: " + bookedVehicle.getVehicleID());
    }

    private void updatePriceBasedOnUserType(Vehicle bookedVehicle, String userType) {
        if ("Teacher".equals(userType) || "Student".equals(userType)) {
            priceTextView.setText("Price: " + (bookedVehicle.getBasePrice() / 2));
        } else {
            priceTextView.setText("Price: " + bookedVehicle.getBasePrice());
        }
    }

    private void addBookedVehicleToUser(Vehicle bookedVehicle) {
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .update("bookedVehicles", FieldValue.arrayUnion(bookedVehicle.getVehicleID()))
                    .addOnSuccessListener(aVoid -> Toast.makeText(VehicleProfileActivity.this, "Vehicle added to your profile", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(VehicleProfileActivity.this, "Failed to add vehicle to profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
