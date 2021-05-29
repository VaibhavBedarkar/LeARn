package com.vaibhavbedarkar.learn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;

public class Dashboard extends AppCompatActivity {
    ImageButton logout, settings;
    Button arLaunch, generate_btn;
    TextView law_statement, law, displayUserName, displayUserEmail;
    String law_name[] = {"Archimedes Principle", "Avogadro’s Law", "Ohm's Law", "Newton’s First law of Motion", "Newton’s Second Law of Motion", "Newton’s Third Law of Motion", "Newton’s Law of cooling", "Coulomb’s Law", "Stefan’s Law", "Pascal’s Law", "Hooke’s Law", "Bernoulli's Principle", "Boyle's Law", " Charles's Law", "Kepler's Law", "Law of conservation of energy", "Tyndall effect", "Graham’s Law"};
    String law_collection[] = {"When a body is partially or totally immersed in a fluid, it experiences an upward thrust equal to the weight of the fluid displaced by it that i.e. its apparent loss of weight is equal to the weight of liquid displaced.", "equal volume of all gases under the same conditions of temperature and pressure contain equal number of molecules.", "the current passing through a conductor between two points is directly proportional to the potential difference across the two points provided the physical state and temperature etc. of the conductor does not change.", "A body continues in its state of rest, or of uniform motion in a straight line, except in so far as it is compelled by external impressed forces to change that state. It is also called Law of Inertia.", "The rate of change of momentum is proportional to the impressed force and takes place in the direction of the straight line in which the force acts. In other words “Force is equal to mass multiplied by acceleration”.", "To every action there is equal and opposite reaction.", "The rate at which a body cools or loses its heat to its surroundings is proportional to the excess of mean temperature of the body over that of the surroundings, provided this temperature excess is not too large.", "The force between the two electric charges reduces to a quarter of its former value when the distance between them is doubled. The SI unit of electric charge, coulomb", "The total energy radiated from a black body is equal to the fourth power of its absolute temperature.", "When pressure is applied to a fluid, the pressure change is transmitted to every part of the fluid without loss.  Atmospheric pressure decreases with increase in height. The SI unit of pressure is pascal", " the extension of a spring is proportional to the tension stretching it. Doubling of the tension results in the doubling of the amount of stretch.", "the speed of a moving fluid, liquid or gas, increases, the pressure within the fluid decreases.", "temperature remaining constant, volume of a given mass of a gas varies inversely with the pressure of the gas.", "pressure remaining constant, the volume of a given mass of gas increases or decreases by 1/273 part of its volume at 0 degree Celsius for each degree Celsius rise or fall of its temperature.", "Each planet revolves round the Sun in an elliptical orbit with the Sun at one focus. The straight line joining the Sun and the planet sweeps out equal areas in equal intervals. The squares of the orbital periods of planets are proportional to the cubes of their mean distance from the Sun.", "energy can neither be created nor destroyed but it can be transformed from one form to another.", "The scattering of light by very small particles suspended in a gas or liquid.", "the rates of diffusion of gases are inversely proportional to the square roots of their densities under similar conditions of temperature and pressure."};
    String currentLaw, currentLawStatement;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(Dashboard.this);
        alertDialogue.setTitle("Exit");
        alertDialogue.setIcon(R.drawable.startup_logo);
        alertDialogue.setMessage("Are you Sure you want to Exit??");
        alertDialogue.setCancelable(true);

        alertDialogue.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }

        });
        alertDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogue.create();
        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore userDatabase = FirebaseFirestore.getInstance();
        String email = user.getEmail();

        CollectionReference citiesRef = userDatabase.collection("UserDetails");

        Query query = citiesRef.whereEqualTo("Name", email);

        String name = user.getDisplayName();

        displayUserName = findViewById(R.id.userName);
        displayUserEmail = findViewById(R.id.userEmail);

        displayUserEmail.setText(email);
        displayUserName.setText(name);

        law_statement = findViewById(R.id.law_statement);
        law = findViewById(R.id.law);

        Random random = new Random();

        currentLaw = law_name[8];
        currentLawStatement = law_collection[8];
        law.setText(currentLaw);
        law_statement.setText(currentLawStatement);

        settings = findViewById(R.id.settings);
        logout = findViewById(R.id.logout);
        arLaunch = findViewById(R.id.extended_fab);
        generate_btn = findViewById(R.id.generate_btn);

        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNo = random.nextInt(14);
                currentLaw = law_name[randomNo];
                currentLawStatement = law_collection[randomNo];
                law.setText(currentLaw);
                law_statement.setText(currentLawStatement);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this, Settings.class);
                startActivity(in);
                finish();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                finish();

            }
        });

        arLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ArActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean isARCoreSupportedAndUpToDate() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        switch (availability) {
            case SUPPORTED_INSTALLED:
                return true;

            case SUPPORTED_APK_TOO_OLD:
            case SUPPORTED_NOT_INSTALLED:
                try {

                    ArCoreApk.InstallStatus installStatus = ArCoreApk.getInstance().requestInstall(this, true);
                    switch (installStatus) {
                        case INSTALL_REQUESTED:
                            Toast.makeText(Dashboard.this, "ARCore installation requested.", Toast.LENGTH_SHORT).show();
                            return false;
                        case INSTALLED:
                            return true;
                    }
                } catch (UnavailableException e) {
                    Toast.makeText(Dashboard.this, "ARCore not installed", Toast.LENGTH_SHORT).show();
                }
                return false;

            case UNSUPPORTED_DEVICE_NOT_CAPABLE:
                Toast.makeText(Dashboard.this, "This device is not supported by ARCore .", Toast.LENGTH_SHORT).show();
                return false;

            case UNKNOWN_CHECKING:
            case UNKNOWN_ERROR:
            case UNKNOWN_TIMED_OUT:
                Toast.makeText(Dashboard.this, "Check for Internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}