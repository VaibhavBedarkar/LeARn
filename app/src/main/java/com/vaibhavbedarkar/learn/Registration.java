package com.vaibhavbedarkar.learn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText user_name, user_email, user_password, user_mobile;
    String userName, userEmail, userPassword, userGender, userMobile, userGroup;
    com.google.android.material.textfield.TextInputLayout tl_name, tl_email, tl_password, tl_mobile;
    Spinner user_Group;
    Button registerUser;
    RadioGroup genderGroup;
    RadioButton user_gender;
    String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
    ProgressBar progress_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_registration);

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_mobile = findViewById(R.id.user_mobile);

        tl_name = findViewById(R.id.tl_name);
        tl_email = findViewById(R.id.tl_email);
        tl_password = findViewById(R.id.tl_password);
        tl_mobile = findViewById(R.id.tl_mobile);

        progress_register = findViewById(R.id.progress_register);

        user_Group = findViewById(R.id.user_group);

        genderGroup = findViewById(R.id.genderGroup);

        registerUser = findViewById(R.id.registerUser);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.group, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        user_Group.setAdapter(adapter);
        user_Group.setOnItemSelectedListener(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseFirestore userDatabase = FirebaseFirestore.getInstance();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_register.setVisibility(View.VISIBLE);

                if (nameValidation() && emailValidation() && passwordValidation() && genderValidation() && mobileValidation()) {
                    userMobile = user_mobile.getText().toString();
                    userName = user_name.getText().toString().trim();
                    userPassword = user_password.getText().toString().trim();
                    userEmail = user_email.getText().toString().trim();

                    Map<String, Object> userInfo = new HashMap<>();

                    userInfo.put("Name", userName);
                    userInfo.put("Email", userEmail);
                    userInfo.put("Gender", userGender);
                    userInfo.put("Mobile", userMobile);
                    userInfo.put("Group", userGroup);

                    userDatabase.collection("UserDetails").add(userInfo);

                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(Registration.this, "User Created Successfully!!!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registration.this, Successful.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Registration.this, "Problem in Registration please try Again!!", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                } else {
                    Toast.makeText(Registration.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public boolean mobileValidation() {
        userMobile = user_mobile.getText().toString();

        if (userMobile.isEmpty()) {
            user_mobile.setError("Field can't be empty");
            return false;
        } else if (userMobile.length() < 10) {
            user_mobile.setError("Enter valid phone number");
            return false;
        } else {
            user_mobile.setError(null);
            return true;
        }
    }

    public boolean nameValidation() {
        userName = user_name.getText().toString().trim();
        if (userName.isEmpty()) {
            user_name.setError("Name is empty");
            return false;
        } else {
            user_name.setError(null);
            return true;
        }

    }

    public boolean passwordValidation() {
        userPassword = user_password.getText().toString().trim();

        if (userPassword.isEmpty() && !userPassword.matches("^[A-Za-z]\\w{7,15}$")) {
            user_password.setError("Field empty or not as per format");
            return false;
        } else {
            user_password.setError(null);
            return true;
        }
    }

    public boolean emailValidation() {
        userEmail = user_email.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (userEmail.isEmpty()) {
            user_email.setError("Field can not be empty");
            return false;
        } else if (!userEmail.matches(checkEmail)) {
            user_email.setError("Invalid Email!");
            return false;
        } else {
            user_email.setError(null);
            return true;
        }
    }

    public boolean genderValidation() {
        int selectedRadioButtonId = genderGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            user_gender = findViewById(selectedRadioButtonId);
            userGender = user_gender.getText().toString().trim();
            return true;

        } else {
            return false;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userGroup = parent.getItemAtPosition(position).toString().trim();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
