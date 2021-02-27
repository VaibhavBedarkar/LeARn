package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Successful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);


        Button return_btn = findViewById(R.id.clickToLogin);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Successful.this, LoginRegistration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}