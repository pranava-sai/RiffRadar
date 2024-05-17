package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The PaymentCompleteActivity class represents an activity that is displayed when a payment process
 * is completed. It provides functionality to navigate back to different feed activities based on
 * the user type.
 */
public class PaymentCompleteActivity extends AppCompatActivity {
    private String amtReceived;
    private String orderNumber;
    private TextView amt;
    private TextView orderNum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_pay);

        amt = findViewById(R.id.amountToPay);
        orderNum = findViewById(R.id.number);

        amtReceived = getIntent().getStringExtra("Received");
        orderNumber = getIntent().getStringExtra("Order Number");
        amt.setText(amtReceived);
        orderNum.setText(orderNumber);

//        Button myBook = findViewById(R.id.myBookings);
        Button feed = findViewById(R.id.toFeed);

        /* onClick listener on login button pressed */
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCategory = UserInfo.getInstance().loggedInUser.loginInfo.userType;

                Class destination;
                switch (userCategory) {
                    case "attendee":
                        destination = GuestFeedActivity.class;
                        break;
                    case "band":
                        destination = BandFeedActivity.class;
                        break;
                    default:
                        destination = null;
                }

                /* when login button is pressed, use intent to switch to MainActivity Activity */
                Intent intent2 = new Intent(PaymentCompleteActivity.this, destination);
                startActivity(intent2);  // go to respective feed pages
            }
        });
    }
}
