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
                        db.collection("vehicle").document(vehicleId).get().addOnCompleteListener(task1 -> {
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
        // 创建一个新的CardView实例
        CardView cardView = new CardView(RideHistoryActivity.this);
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLayoutParams.setMargins(30, 20, 30, 20);
        cardView.setLayoutParams(cardLayoutParams);

        float cardElevation = getResources().getDimension(R.dimen.card_elevation);
        float cardCornerRadius = getResources().getDimension(R.dimen.card_corner_radius);

        cardView.setCardElevation(cardElevation);
        cardView.setRadius(cardCornerRadius);
        cardView.setUseCompatPadding(true);

        // 创建LinearLayout
        LinearLayout linearLayout = new LinearLayout(RideHistoryActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(10, 10, 10, 10);

        // 根据需要动态添加TextView
        TextView vehicleTypeTextView = createTextView(vehicle.getVehicleType());
        TextView modelTextView = createTextView(vehicle.getModel());
        TextView vehicleIDTextView = createTextView(vehicle.getVehicleID());
        TextView priceTextView = createTextView(String.valueOf(calculatePrice(vehicle, userType)));

        // 将TextView添加到LinearLayout
        linearLayout.addView(vehicleTypeTextView);
        linearLayout.addView(modelTextView);
        linearLayout.addView(vehicleIDTextView);
        linearLayout.addView(priceTextView);

        // 将LinearLayout添加到CardView
        cardView.addView(linearLayout);

        // 将CardView添加到rideHistoryLayout中
        rideHistoryLayout.addView(cardView);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(RideHistoryActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.black)); // 确保您的项目中有这个颜色
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
