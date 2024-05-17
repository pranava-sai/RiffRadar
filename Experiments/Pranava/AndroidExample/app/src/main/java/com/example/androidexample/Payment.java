package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
//import android.widget.Toast;

public class Payment extends AppCompatActivity {

    private String userCategory;

    private ImageView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        /* initialize UI elements */
        // define email variable
        EditText email = findViewById(R.id.email_edt);  // link to username edit text in the Payment activity XML
        // define card number variable
        EditText cardNum = findViewById(R.id.card_num_edt);  // link to password edit text in the Payment activity XML
        // define expiry date variable
        EditText expiryDate = findViewById(R.id.expire_edt);    // link to confirm edit text in the Payment activity XML
        // define cvc variable
        EditText securityCode = findViewById(R.id.cvc_edt);    // link to login button in the Payment activity XML
        // define card holder name variable
        EditText cardHolderName = findViewById(R.id.cardHolder_name_edt);  // link to signup button in the Payment activity XML
        // define address street 1 variable
        EditText street1 = findViewById(R.id.address_street1_edt);    // link to login button in the Payment activity XML
        // define address street 2 variable
        EditText street2 = findViewById(R.id.address_street2_edt);  // link to signup button in the Payment activity XML
        // define city variable
        EditText city = findViewById(R.id.city_edt);  // link to signup button in the Payment activity XML
        // define dropdown for states
        Spinner state = findViewById(R.id.state_edt); // Dropdown for user type selection
        // define zip code variable
        EditText zipcode = findViewById(R.id.zip_edt);  // link to pay button in the Payment activity XML
        // define pay button
        Button pay = findViewById(R.id.pay_btn);

        banner = findViewById(R.id.banner);



//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            banner.setImageResource(R.drawable.rifflight);
//        } else {
//            banner.setImageResource(R.drawable.riff);
//        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                banner.setImageResource(R.drawable.rifflight);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                banner.setImageResource(R.drawable.riff);
                break;
        }


        String [] statesArray = {"Select State","AK","AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI",
                "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY"};
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statesArray);
        state.setAdapter(userAdapter);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             *
             *           number of player is allowed 2 to 10
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userCategory = parent.getItemAtPosition(position).toString();
            }

            /**
             *
             * @param parent The AdapterView that now contains no selected item.
             *               no item selected so do nothing
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* click listener on login button pressed */
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the pay button is pressed, navigate to the PaymentComplete activity
                Intent intent = new Intent(Payment.this, PaymentCompleteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            banner.setImageResource(R.drawable.rifflight);
//        } else {
//            banner.setImageResource(R.drawable.riff);
//        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                banner.setImageResource(R.drawable.rifflight);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                banner.setImageResource(R.drawable.riff);
                break;
        }
    }
}