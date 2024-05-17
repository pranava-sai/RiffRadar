package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GreeterActivity extends AppCompatActivity {

    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable
    private TextView guestButton, fanButton, bandButton, venueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeter);             // link to Main activity XML

        /* initialize UI elements */
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML

        guestButton = findViewById(R.id.guestButton);
        fanButton = findViewById(R.id.fanButton);
        bandButton = findViewById(R.id.bandButton);
        venueButton = findViewById(R.id.hostButton);

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(GreeterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on guest button pressed */
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, GuestFeedActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on fan button pressed */
        fanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, FanFeedActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on venue button pressed */
        venueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, VenueFeedActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on band button pressed */
        bandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, BandFeedActivity.class);
                startActivity(intent);
            }
        });

    }
}