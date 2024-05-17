package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView name;

    private Button nextButton;     // define next button variable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        name = findViewById(R.id.user_name);// link to username textview in the Main activity XML
        nextButton = findViewById(R.id.next);    // link to login button in the Main activity XML


        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        messageText.setText("Welcome to Earth");
        if(extras == null) {
            name.setVisibility(View.VISIBLE);             // set username text invisible initially
        } else {
            name.setText(extras.getString("USERNAME")); // this will come from LoginActivity
            nextButton.setVisibility(View.VISIBLE);              // set login button invisible
                      // set signup button invisible
        }

        /* click listener on login button pressed */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
}}

