package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText feedback_topicName, feedback_suggestion;
    Spinner feedback_subject;
    RatingBar feedback_ratingBar;
    String topicName, suggestion, subject;
    Float rating;
    Button feedback_submit;
    ProgressBar feedback_progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_topicName = findViewById(R.id.feedback_topicname);
        feedback_suggestion = findViewById(R.id.feedback_suggestion);
        feedback_subject = findViewById(R.id.feedback_subject);
        feedback_ratingBar = findViewById(R.id.feedback_rating);
        feedback_submit = findViewById(R.id.submit);

        feedback_progress_bar = findViewById(R.id.feedback_progress_bar);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.subject, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        feedback_subject.setAdapter(adapter);
        feedback_subject.setOnItemSelectedListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback_progress_bar.setVisibility(View.VISIBLE);

                topicName = feedback_topicName.getText().toString();
                suggestion = feedback_suggestion.getText().toString();
                rating = feedback_ratingBar.getRating();

                Map feedbackData = new HashMap<>();

                feedbackData.put("Topic Name", topicName);
                feedbackData.put("Topic Subject", subject);
                feedbackData.put("Topic Suggestion", suggestion);
                feedbackData.put("Rating", rating);

                db.collection("UserFeedback").add(feedbackData);

                Toast.makeText(Feedback.this, "Feedback Submitted Successfully!!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Feedback.this, Dashboard.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        subject = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}