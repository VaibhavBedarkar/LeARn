package com.vaibhavbedarkar.learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegistration extends AppCompatActivity {

    Button forgot_password, button_registration, button_login;

    EditText userEmail, userPassword;
    String email, password;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login_registration);

        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);

        forgot_password = findViewById(R.id.button_forgotpassword);
        button_login = findViewById(R.id.button_login);
        button_registration = findViewById(R.id.button_registration);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginRegistration.this, Dashboard.class);
            startActivity(intent);
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = userEmail.getText().toString();
                password = userPassword.getText().toString();

                if (validateEmail() && validatePassword())
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginRegistration.this, "Welcome " + email, Toast.LENGTH_LONG).show();
                                Intent in = new Intent(LoginRegistration.this, Dashboard.class);
                                startActivity(in);
                                finish();
                            } else {

                                Toast.makeText(LoginRegistration.this, "Auth Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            }
        });

        button_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegistration.this, Registration.class);
                startActivity(intent);
            }
        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(LoginRegistration.this, "Enter Registered Email", Toast.LENGTH_SHORT).show();

                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginRegistration.this, "Email reset link to registered email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginRegistration.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }

    private boolean validateEmail() {

        email = userEmail.getText().toString();

        if (email.isEmpty()) {
            userEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            userEmail.setError("Invalid email address");
            return false;
        } else {
            userEmail.setError(null);
            return true;
        }

    }


    private boolean validatePassword() {
        password = userPassword.getText().toString();

        if (password.isEmpty()) {
            userPassword.setError("Password field cannot be empty");
            return false;
        }

        return true;
    }

}