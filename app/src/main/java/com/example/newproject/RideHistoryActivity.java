package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RideHistoryActivity extends AppCompatActivity {

    private Button backButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        currentUser = auth.getCurrentUser();

        ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();

        db.collection("user").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        List<String> bookedVehicles = (List<String>) snapshot.get("bookedVehicles");
                        for(String s : bookedVehicles){
                            db.collection("vehicle").whereEqualTo("vehicleID", s).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful() && task.getResult() != null){
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Vehicle vehicle = document.toObject(Vehicle.class);
                                            vehicleArrayList.add(vehicle);
                                        }
                                        for(Vehicle v : vehicleArrayList){

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        backButton = this.findViewById(R.id.back_button_rideHistoryActivity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RideHistoryActivity.this, ProfilePageActivity.class));
            }
        });
    }
}