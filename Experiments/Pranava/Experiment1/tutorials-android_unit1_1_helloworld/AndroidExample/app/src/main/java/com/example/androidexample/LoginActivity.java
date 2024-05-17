package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText nameEntry;  // define username edittext variable
    private Button nextButton;         // define login button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);            // link to Login activity XML

        /* initialize UI elements */
        nameEntry = findViewById(R.id. user_name);
        nextButton = findViewById(R.id.next);    // link to login button in the Login activity XML// link to signup button in the Login activity XML

        /* click listener on login button pressed */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = nameEntry.getText().toString();

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("Name:", username);  // key-value to pass to the MainActivity
                startActivity(intent);  // go to MainActivity with the key-value data
            }
        });
    }
}