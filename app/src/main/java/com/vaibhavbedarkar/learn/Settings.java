package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button changePassword, feedbackForm, deleteAccount;
    EditText getPassword;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changePassword = findViewById(R.id.change_password);
        feedbackForm = findViewById(R.id.feedback);
        deleteAccount = findViewById(R.id.delete_account);
        getPassword = findViewById(R.id.ch_password);



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        feedbackForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Feedback.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = getPassword.getText().toString().trim();
                Toast.makeText(Settings.this,"Password "+password,Toast.LENGTH_SHORT).show();

//                if(user !=null && !password.equals("")){
//                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(Settings.this,"Password Changed Successfully!!!",Toast.LENGTH_SHORT).show();
//                                firebaseAuth.signOut();
//                            }else{
//                                Toast.makeText(Settings.this,"Failed to update password!!",Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    });
//                }

            }
        });


    }
}