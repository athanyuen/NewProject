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

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LinearLayout linearLayout;
    private EditText editNameField;
    private String roleChosen;
    private EditText graduateYear;
    private EditText inSchoolTitle;
    private EditText parentUIDs;
    private EditText studentUIDs;
    private Button changeRoleButton, backButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        linearLayout = findViewById(R.id.linearLayout);
        spinner = findViewById(R.id.spinner);
        changeRoleButton = findViewById(R.id.change_Role_Button);
        backButton = findViewById(R.id.back_button_userProfileActivity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            }
        });

        setRoleSpinner();

        changeRoleButton.setOnClickListener(v -> updateUserProfile());
    }

    public void updateUserProfile() {
        String name = editNameField.getText().toString();
        String userID = mUser.getUid();

        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);

        switch (roleChosen) {
            case "Alumni":
                user.put("graduateYear", graduateYear.getText().toString());
                break;
            case "Teacher":
                user.put("inSchoolTitle", inSchoolTitle.getText().toString());
                break;
            case "Student":
                user.put("graduateYear", graduateYear.getText().toString());
                user.put("parentUIDs", parentUIDs.getText().toString());
                break;
            case "Parent":
                user.put("studentUIDs", studentUIDs.getText().toString());
                break;
        }

        db.collection("users").document(userID)
                .update(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show());
    }

    private void setRoleSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.userTypes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleChosen = parent.getItemAtPosition(position).toString();
                differentRowFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void differentRowFields() {
        linearLayout.removeAllViews();

        editNameField = new EditText(this);
        editNameField.setHint("Name");
        linearLayout.addView(editNameField);

        switch (roleChosen) {
            case "Alumni":
                graduateYear = new EditText(this);
                graduateYear.setHint("Graduate Year");
                linearLayout.addView(graduateYear);
                break;
            case "Teacher":
                inSchoolTitle = new EditText(this);
                inSchoolTitle.setHint("In School Title");
                linearLayout.addView(inSchoolTitle);
                break;
            case "Student":
                graduateYear = new EditText(this);
                graduateYear.setHint("Graduate Year");
                linearLayout.addView(graduateYear);
                parentUIDs = new EditText(this);
                parentUIDs.setHint("Parent UID");
                linearLayout.addView(parentUIDs);
                break;
            case "Parent":
                studentUIDs = new EditText(this);
                studentUIDs.setHint("Student UID");
                linearLayout.addView(studentUIDs);
                break;
        }
    }
}
