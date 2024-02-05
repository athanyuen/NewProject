package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePageActivity extends AppCompatActivity {
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

        signOutButton = this.findViewById(R.id.sign_out_button_profilePage);
        backButton = this.findViewById(R.id.back_button_profilePageActivity);
        rideHistoryButton = this.findViewById(R.id.ride_history_button);

        nameTextView = this.findViewById(R.id.name_profilePage);
        uidTextView = this.findViewById(R.id.uid_profilePage);
        usertypeTextView = this.findViewById(R.id.usertype_profilePage);
        ridestakenTextView = this.findViewById(R.id.ridesTaken_profilePage);
        ownedvehiclesTextView = this.findViewById(R.id.ownedvehicles_profilePage);
        emailTextView = this.findViewById(R.id.email_profilePage);

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                User currentUser = snapshot.toObject(User.class);
                nameTextView.setText(String.valueOf(currentUser.getName()));
                uidTextView.setText(String.valueOf(currentUser.getUid()));
                usertypeTextView.setText(String.valueOf(currentUser.getUserType()));
                emailTextView.setText(String.valueOf(currentUser.getEmail()));
            }
        });






        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePageActivity.this, MainActivity.class));
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfilePageActivity.this, SignupActivity.class));
            }
        });

        rideHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePageActivity.this, RideHistoryActivity.class));
            }
        });

    }

}