package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class BillingActivity extends AppCompatActivity {

    private TextView toPay;
    private EditText streetAddress;
    private EditText additionalAddress;
    private EditText city;
    private EditText zipCode;
    private Spinner state;
    private Button next;
    private String amt;
    private String emailId;
    private String stateSelected;
    private String[] orderInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        amt = getIntent().getStringExtra("Total");
        toPay = findViewById(R.id.amountToPay);
        toPay.setText(amt);
        emailId = getIntent().getStringExtra("Email ID");
        next = findViewById(R.id.toPayment);
        streetAddress = findViewById(R.id.address1);
        additionalAddress = findViewById(R.id.address2);
        city = findViewById(R.id.city);
        zipCode = findViewById(R.id.zip);
        state = findViewById(R.id.state_edt);
        orderInfo = getIntent().getStringArrayExtra("Order Info");

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
                stateSelected = parent.getItemAtPosition(position).toString();
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String street1 = streetAddress.getText().toString();
                String street2 = additionalAddress.getText().toString();
                String cityEntry = city.getText().toString();
                String zip = zipCode.getText().toString();
                Intent intent = new Intent(BillingActivity.this, Payment.class);
                String[] orderAndShippingInfo = {orderInfo[0], orderInfo[1], orderInfo[2], orderInfo[3], orderInfo[4], street1, street2, cityEntry, stateSelected, zip};
                intent.putExtra("Complete Details", orderAndShippingInfo);
                startActivity(intent);
            }
        });

    }
}