package com.vaibhavbedarkar.learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    EditText userEmail,userPassword;
    String email,password;

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
        if(currentUser!=null){
            Intent intent = new Intent(LoginRegistration.this, Dashboard.class);
            startActivity(intent);
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = userEmail.getText().toString();
                password = userPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginRegistration.this,"Welcome "+email,Toast.LENGTH_LONG).show();
                            Intent in = new Intent(LoginRegistration.this,Dashboard.class);
                            startActivity(in);
                            finish();
                        }
                        else{

                            Toast.makeText(LoginRegistration.this,"Auth Failed",Toast.LENGTH_SHORT).show();

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


    }

}