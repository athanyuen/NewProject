package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class ProfilePageActivity extends AppCompatActivity {
    private static final String TAG = "ProfilePageActivity"; // For logging
    private Button signOutButton, backButton, rideHistoryButton;
    private TextView nameTextView, uidTextView, usertypeTextView, ridestakenTextView, ownedvehiclesTextView, emailTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); // Get the current user

        if (currentUser == null) {
            // If no user is logged in, redirect to the sign-in or sign-up screen
            startActivity(new Intent(this, SignupActivity.class));
            finish(); // Prevent the user from returning to this activity without being logged in
            return;
        }

        signOutButton = findViewById(R.id.sign_out_button_profilePage);
        backButton = findViewById(R.id.back_button_profilePageActivity);
        rideHistoryButton = findViewById(R.id.ride_history_button);

        nameTextView = findViewById(R.id.name_profilePage);
        uidTextView = findViewById(R.id.uid_profilePage);
        usertypeTextView = findViewById(R.id.usertype_profilePage);
        ridestakenTextView = findViewById(R.id.ridesTaken_profilePage);
        ownedvehiclesTextView = findViewById(R.id.ownedvehicles_profilePage);
        emailTextView = findViewById(R.id.email_profilePage);

        db = FirebaseFirestore.getInstance();
        loadUserProfile();

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePageActivity.this, MainActivity.class));
        }); // Use the default back behavior

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfilePageActivity.this, SignupActivity.class));
            finish(); // Clear this activity off the stack
        });

        rideHistoryButton.setOnClickListener(v -> startActivity(new Intent(ProfilePageActivity.this, RideHistoryActivity.class)));
    }

    private void loadUserProfile() {
        String userId = currentUser.getUid(); // Get the UID for the current user
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) { // Check if the user document exists
                    User user = snapshot.toObject(User.class);
                    if (user != null) { // Ensure the user object is not null
                        nameTextView.setText(user.getName());
                        uidTextView.setText(mAuth.getUid());
                        usertypeTextView.setText(user.getUserType());
                        emailTextView.setText(user.getEmail());
                        setRideCountTextView();
                    } else {
                        Log.e(TAG, "User data is null!");
                        Toast.makeText(ProfilePageActivity.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Snapshot does not exist.");
                    Toast.makeText(ProfilePageActivity.this, "Profile does not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Failed to load user profile.", task.getException());
                Toast.makeText(ProfilePageActivity.this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRideCountTextView(){
        String userID = currentUser.getUid();
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        List<String> bookedVehicles = (List<String>) snapshot.get("bookedVehicles");
                        int rideCount = bookedVehicles.size();
                        ridestakenTextView.setText("Ride Count: " + String.valueOf(rideCount));
                    }
                }
            }
        });

    }
}
