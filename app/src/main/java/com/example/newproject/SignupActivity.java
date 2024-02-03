package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db.setFirestoreSettings(settings);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if(userEmail.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if(pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }

                auth.createUserWithEmailAndPassword(userEmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                        User newUser = new User(userEmail, pass);
                        db.collection("users").document(user.getUid()).set(newUser);
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        }else{
                            Toast.makeText(SignupActivity.this, "Signup Failed" + task.getException().getMessage(),Toast.LENGTH_SHORT ).show();
                        }

                    }
                });

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}