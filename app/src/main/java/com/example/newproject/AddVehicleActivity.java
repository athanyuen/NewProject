package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddVehicleActivity extends AppCompatActivity {

    private FirebaseFirestore firestoreRef;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button backButton, addVehicleButton;
    private LinearLayout linearLayout;
    private EditText ownerEdit, modelEdit, capacityEdit, vehicleIDEdit, riderUIDsEdit, basePriceEdit, carRangeEdit, electricCarRangeEdit, maxAltitudeEdit, maxAirSpeedEdit, bicycleTypeEdit, bicycleWeightEdit, bicycleWeightCapacityEdit, segwayRangeEdit, segwayWeightCapacityEdit;
    private String vehicleTypeChosen;
    private Spinner vehicleTypeSpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestoreRef = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.back_button_addVehicleActivity);
        addVehicleButton = findViewById(R.id.add_vehicle_button);
        vehicleTypeSpinner = findViewById(R.id.vehicle_type_spinner);
        linearLayout = findViewById(R.id.add_vehicle_linear_layout); // 确保LinearLayout初始化

        setRoleSpinner();

        backButton.setOnClickListener(v -> startActivity(new Intent(AddVehicleActivity.this, MainActivity.class)));

        addVehicleButton.setOnClickListener(v -> {
            if (mUser != null) {
                updateVehicleType();
            } else {
                Toast.makeText(AddVehicleActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateVehicleType() {
        String owner = ownerEdit.getText().toString();
        String model = modelEdit.getText().toString();
        String capacity = capacityEdit.getText().toString();
        String vehicleID = vehicleIDEdit.getText().toString();
        String riderUIDs = riderUIDsEdit.getText().toString();
        String basePrice = basePriceEdit.getText().toString();


        String userID = mUser.getUid();


        HashMap<String, Object> vehicle = new HashMap<>();
        vehicle.put("owner", owner);
        vehicle.put("model", model);
        vehicle.put("capacity", capacity);
        vehicle.put("vehicleID", vehicleID);
        vehicle.put("riderUIDs", riderUIDs);
        vehicle.put("basePrice", basePrice);

        switch (vehicleTypeChosen) {
            case "Car":
                vehicle.put("carRange", carRangeEdit.getText().toString());
                break;
            case "Electric Car":
                vehicle.put("carRange", electricCarRangeEdit.getText().toString());
                break;
            case "Helicopter":
                vehicle.put("maxAltitude", maxAltitudeEdit.getText().toString());
                vehicle.put("maxAirSpeed", maxAirSpeedEdit.getText().toString());
                break;
            case "Bicycle":
                vehicle.put("bicycleType", bicycleTypeEdit.getText().toString());
                vehicle.put("bicycleWeight", bicycleWeightEdit.getText().toString());
                vehicle.put("bicycleWeightCapacity", bicycleWeightCapacityEdit.getText().toString());
                break;
            case "Segway":
                vehicle.put("segwayRange", segwayRangeEdit.getText().toString());
                vehicle.put("segwayWeightCapacity", segwayWeightCapacityEdit.getText().toString());
                break;

        }

        db.collection("vehicle").document(userID)
                .set(vehicle)
                .addOnSuccessListener(aVoid -> Toast.makeText(AddVehicleActivity.this, "Vehicles Updated Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddVehicleActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show());
    }

    private void setRoleSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.vehicleTypes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(arrayAdapter);

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleTypeChosen = parent.getItemAtPosition(position).toString();
                differentVehicleFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void differentVehicleFields() {
        linearLayout.removeAllViews();

        ownerEdit = new EditText(this);
        ownerEdit.setHint("Owner");
        linearLayout.addView(ownerEdit);
        modelEdit = new EditText(this);
        modelEdit.setHint("Model");
        linearLayout.addView(modelEdit);
        capacityEdit = new EditText(this);
        capacityEdit.setHint("Capacity");
        linearLayout.addView(capacityEdit);
        vehicleIDEdit = new EditText(this);
        vehicleIDEdit.setHint("Vehicle ID");
        linearLayout.addView(vehicleIDEdit);
        riderUIDsEdit = new EditText(this);
        riderUIDsEdit.setHint("Rider UID");
        linearLayout.addView(riderUIDsEdit);
        basePriceEdit = new EditText(this);
        basePriceEdit.setHint("Base Price");
        linearLayout.addView(basePriceEdit);



        switch (vehicleTypeChosen) {
            case "Car":
                carRangeEdit = new EditText(this);
                carRangeEdit.setHint("Car Range");
                linearLayout.addView(carRangeEdit);
                break;
            case "Electric Car":
                electricCarRangeEdit = new EditText(this);
                electricCarRangeEdit.setHint("Car Range");
                linearLayout.addView(electricCarRangeEdit);
                break;
            case "Helicopter":
                maxAirSpeedEdit = new EditText(this);
                maxAirSpeedEdit.setHint("Maximum Air Speed");
                linearLayout.addView(maxAirSpeedEdit);
                maxAltitudeEdit = new EditText(this);
                maxAltitudeEdit.setHint("Maximum Altitude");
                linearLayout.addView(maxAltitudeEdit);
                break;
            case "Bicycle":
                bicycleTypeEdit = new EditText(this);
                bicycleTypeEdit.setHint("Bicycle Type");
                linearLayout.addView(bicycleTypeEdit);
                bicycleWeightEdit = new EditText(this);
                bicycleWeightEdit.setHint("Bicycle Weight");
                linearLayout.addView(bicycleWeightEdit);
                bicycleWeightCapacityEdit = new EditText(this);
                bicycleWeightCapacityEdit.setHint("Bicycle Weight Capacity");
                linearLayout.addView(bicycleWeightCapacityEdit);
                break;
            case "Segway":
                segwayRangeEdit = new EditText(this);
                segwayRangeEdit.setHint("Segway Range");
                linearLayout.addView(segwayRangeEdit);
                segwayWeightCapacityEdit = new EditText(this);
                segwayWeightCapacityEdit.setHint("Segway Weight Capacit");
                linearLayout.addView(segwayWeightCapacityEdit);

        }
    }
}