package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton, signupWithGoogleButton;
    private TextView loginRedirectText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 20;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupWithGoogleButton = this.findViewById(R.id.signup_google_button);
        setupGoogleSignIn();

        signupWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


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
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
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
    private void setupGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task ->{
                    if(task.isSuccessful()){
                        Log.d("Google Sign In", "signInWithCredential:success");
                        Toast.makeText(SignupActivity.this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = auth.getCurrentUser();
                        if(user != null){
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                         if(user != null){
                             String userID = user.getUid();
                             String userEmail = user.getEmail();
                             String userName = user.getDisplayName();

                             Map<String, Object> userInfo = new HashMap<>();
                             userInfo.put("uid", userID);
                             userInfo.put("email", userEmail);
                             userInfo.put("name", userName);

                             db.collection("users").document(userID).set(userInfo)
                                     .addOnSuccessListener(aVoid -> Log.d("Firestore", "User info successfully written!"))
                                     .addOnFailureListener(e -> Log.w("Firestore", "Error writing user info", e));
                         }
                    }
                    else{
                        Log.w("Google Sign In", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}