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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VehicleProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Vehicle vehicleInfo;
    private Button backButton;
    private FirebaseFirestore db;
    private Vehicle bookedVehicle;
    private TextView vehicleTypeTextView, ownerTextView, capacityTextView, modelTextView, priceTextView, vehicleIDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        backButton = findViewById(R.id.back_button_vehicleProfileActivity);
        db = FirebaseFirestore.getInstance();

        vehicleTypeTextView = this.findViewById(R.id.vehicle_type);
        ownerTextView = this.findViewById(R.id.owner);
        capacityTextView = this.findViewById(R.id.capacity);
        modelTextView = this.findViewById(R.id.model);
        priceTextView = this.findViewById(R.id.price);
        vehicleIDTextView = this.findViewById(R.id.vehicleID);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VehicleProfileActivity.this, VehicleInfoActivity.class));
            }
        });

        Intent intent = getIntent();
        String selectedVehicle = intent.getStringExtra("selectedVehicle");

        db.collection("vehicle").whereEqualTo("vehicleID", selectedVehicle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        bookedVehicle = document.toObject(Vehicle.class);
                    }
                }

            }
        });
        String uid = "";
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            uid = currentUser.getUid();
        }
        db.collection("users")
                        .document(uid)
                                .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();
                                                    if(document.exists()){
                                                        String userType = document.getString("user type");
                                                        if(userType.equals("Teacher") || userType.equals("Student")){
                                                            priceTextView.setText("Price: " + bookedVehicle.getBasePrice()/2);
                                                        }
                                                        else{
                                                            priceTextView.setText("Price: " + bookedVehicle.getBasePrice());
                                                        }
                                                    }
                                                }
                                                else{

                                                }
                                            }
                                        });

        vehicleTypeTextView.setText("Vehicle Type: " + bookedVehicle.getVehicleType());
        ownerTextView.setText("Owner: " + bookedVehicle.getOwner());
        capacityTextView.setText("Capacity: " + bookedVehicle.getCapacity());
        modelTextView.setText("Model: " + bookedVehicle.getModel());
        vehicleIDTextView.setText("Vehicle ID: " + bookedVehicle.getVehicleID());

    }
}