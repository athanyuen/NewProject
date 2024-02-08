package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RideHistoryActivity extends AppCompatActivity {

    private Button backButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private LinearLayout rideHistoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();
        rideHistoryLayout = findViewById(R.id.rideHistoryLayout);

        backButton = findViewById(R.id.back_button_rideHistoryActivity);
        backButton.setOnClickListener(v -> finish());

        if (currentUser != null) {
            loadRideHistory();
        } else {

            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRideHistory() {
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    List<String> bookedVehicles = (List<String>) snapshot.get("bookedVehicles");
                    String userType = snapshot.getString("userType");
                    for (String vehicleId : bookedVehicles) {

                        db.collection("vehicle").document(vehicleId).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null) {
                                DocumentSnapshot vehicleSnapshot = task1.getResult();
                                if (vehicleSnapshot.exists()) {
                                    Vehicle vehicle = vehicleSnapshot.toObject(Vehicle.class);
                                    addVehicleToView(vehicle, userType);
                                }
                            } else {

                                Toast.makeText(RideHistoryActivity.this, "Failed to load vehicle details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {

                    Toast.makeText(this, "No booked vehicles found", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(RideHistoryActivity.this, "Failed to load user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVehicleToView(Vehicle vehicle, String userType) {

        CardView cardView = new CardView(RideHistoryActivity.this);
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLayoutParams.setMargins(20, 20, 20, 20);
        cardView.setLayoutParams(cardLayoutParams);

        float cardElevation = getResources().getDimension(R.dimen.card_elevation);
        float cardCornerRadius = getResources().getDimension(R.dimen.card_corner_radius);

        cardView.setCardElevation(cardElevation);
        cardView.setRadius(cardCornerRadius);
        cardView.setUseCompatPadding(true);


        LinearLayout linearLayout = new LinearLayout(RideHistoryActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(10, 10, 10, 10);


        TextView vehicleTypeTextView = createTextView(vehicle.getVehicleType());
        TextView modelTextView = createTextView(vehicle.getModel());
        TextView vehicleIDTextView = createTextView(vehicle.getVehicleID());
        TextView priceTextView = createTextView("$" + String.valueOf(calculatePrice(vehicle, userType)));


        linearLayout.addView(vehicleTypeTextView);
        linearLayout.addView(modelTextView);
        linearLayout.addView(vehicleIDTextView);
        linearLayout.addView(priceTextView);


        cardView.addView(linearLayout);


        rideHistoryLayout.addView(cardView);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(RideHistoryActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    private double calculatePrice(Vehicle vehicle, String userType) {
        double price = vehicle.getBasePrice();
        if (userType.equals("Teacher") || userType.equals("Student")) {
            price /= 2;
        }
        return price;
    }
}
