package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PurchaseActivity extends AppCompatActivity {

    private TextView purchasedTxt; // define purchase textview variable
    private Button purchaseBtn; // define purchase button variable
    private Button refundBtn; // define return button variable
    private Button backBtn;     // define back button variable

    private boolean purchased = false;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase);

        /* initialize UI elements */
        purchasedTxt = findViewById(R.id.purchased);
        purchaseBtn = findViewById(R.id.purchase_btn);
        refundBtn = findViewById(R.id.refund_btn);
        backBtn = findViewById(R.id.counter_back_btn);

        /* when purchase btn is pressed, purchased = true, reset number textview */
        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchased = true;
                purchasedTxt.setText(String.valueOf(purchased));
            }
        });

        /* when purchase btn is pressed, purchased = false, reset number textview */
        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchased = false;
                purchasedTxt.setText(String.valueOf(purchased));
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseActivity.this, MainActivity.class);
                intent.putExtra("PURCHASED",purchased);  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

    }
}