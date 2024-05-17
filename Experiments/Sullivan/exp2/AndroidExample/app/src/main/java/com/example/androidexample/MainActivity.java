package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private TextView ticketText;     // define ticket textview variable
    private Button counterButton;     // define counter button variable
    private Button purchaseButton;     // define counter button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        ticketText = findViewById(R.id.main_ticket_txt);      // link to message textview in the Main activity XML
        purchaseButton = findViewById(R.id.main_purchase_btn);// link to purchase button in the Main activity XML

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Intent Example");
        } else {
            String number = extras.getString("NUM");  // this will come from LoginActivity
            messageText.setText("The number was " + number);
        }

        /* extract data passed into this activity from another activity */
        Bundle ticketExtras = getIntent().getExtras();
        if(ticketExtras == null) {
            ticketText.setText("Purchase ticket");
        } else {
            String purchased = extras.getString("PURCHASED");  // this will come from PurchaseActivity
            ticketText.setText("A ticket has been purchased: " + purchased);
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on purchase button pressed */
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when purchase button is pressed, use intent to switch to Purchase Activity */
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });
    }
}