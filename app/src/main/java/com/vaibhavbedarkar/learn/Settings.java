package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button changePassword, feedbackForm, deleteAccount,logout;
    EditText getPassword;
    String password;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changePassword = findViewById(R.id.change_password);
        feedbackForm = findViewById(R.id.feedback);
        deleteAccount = findViewById(R.id.delete_account);
        getPassword = findViewById(R.id.ch_password);
        logout = findViewById(R.id.logout);
        progressBar = findViewById(R.id.progress_bar);



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        feedbackForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Settings.this, Feedback.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                password = getPassword.getText().toString().trim();
                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Settings.this,"Password Updated Successfully!!!",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(Settings.this, Dashboard.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(Settings.this,"Password Updated Successfully!!!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.this, LoginRegistration.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signOut();
                Intent intent = new Intent(Settings.this, LoginRegistration.class);
                startActivity(intent);
                finish();


            }
        });


        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Settings.this,"Sorry to see you go :(",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.this, LoginRegistration.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Settings.this,"Unable to delete account !!",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });







    }


}