package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private LinearLayout rideHistoryLayout; // 用于动态添加车辆信息的布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();
        rideHistoryLayout = findViewById(R.id.rideHistoryLayout); // 确保您在布局文件中设置了这个ID

        backButton = findViewById(R.id.back_button_rideHistoryActivity);
        backButton.setOnClickListener(v -> finish()); // 只需要结束当前Activity即可返回

        if (currentUser != null) {
            loadRideHistory();
        } else {
            // 用户未登录
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
                        // 根据ID查询每辆车的详细信息
                        db.collection("vehicles").document(vehicleId).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult() != null) {
                                DocumentSnapshot vehicleSnapshot = task1.getResult();
                                if (vehicleSnapshot.exists()) {
                                    Vehicle vehicle = vehicleSnapshot.toObject(Vehicle.class);
                                    addVehicleToView(vehicle, userType);
                                }
                            } else {
                                // 查询车辆详细信息失败
                                Toast.makeText(RideHistoryActivity.this, "Failed to load vehicle details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // 用户没有预订的车辆
                    Toast.makeText(this, "No booked vehicles found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 查询用户信息失败
                Toast.makeText(RideHistoryActivity.this, "Failed to load user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addVehicleToView(Vehicle vehicle, String userType) {
        TextView textView = new TextView(this);
        Double price = 0.0;
        if(userType.equals("Teacher") || userType.equals("Student")){
            price = vehicle.getBasePrice()/2;
        }
        else{
            price = vehicle.getBasePrice();
        }
        textView.setText(String.format("%s - %s - %s - %s",
                vehicle.getVehicleType(),
                vehicle.getModel(),
                vehicle.getVehicleID(),
                price));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(16);

        // 将视图添加到LinearLayout中
        rideHistoryLayout.addView(textView);
    }
}
