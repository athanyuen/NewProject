package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePageActivity extends AppCompatActivity {
    private Button signOutButton, backButton, rideHistoryButton;
    private TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        signOutButton = this.findViewById(R.id.sign_out_button_profilePage);
        backButton = this.findViewById(R.id.back_button_profilePageActivity);
        rideHistoryButton = this.findViewById(R.id.ride_history_button);
        phoneNumber = this.findViewById(R.id.phone_number);



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