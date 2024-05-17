package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentCompleteActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payconfirm);

        Button myBook = findViewById(R.id.myBookings);
        Button home = findViewById(R.id.goBack);

        myBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(PaymentCompleteActivity.this, MyBookings.class);
                startActivity(intent);  // go to My Bookings
            }
        });

        /* click listener on login button pressed */
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent2 = new Intent(PaymentCompleteActivity.this, MainActivity.class);
                startActivity(intent2);  // go to LoginActivity
            }
        });
    }
}
