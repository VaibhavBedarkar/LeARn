package com.vaibhavbedarkar.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegistration extends AppCompatActivity {

    Button forgot_password, button_registration, button_login;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login_registration);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        EditText userEmail,userPassword ;
        String email,password;


        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);

        email = userEmail.getText().toString();
        password = userPassword.getText().toString();


        forgot_password = findViewById(R.id.button_forgotpassword);
        button_login = findViewById(R.id.button_login);
        button_registration = findViewById(R.id.button_registration);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //method for firebase authentication

                // firebase session
                Intent intent = new Intent(LoginRegistration.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        button_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegistration.this, Registration.class);
                startActivity(intent);
            }
        });


    }

    private void updateUI() {
    }
}