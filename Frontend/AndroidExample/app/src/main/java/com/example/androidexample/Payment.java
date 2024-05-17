package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import android.widget.Toast;

/**
 * A class representing the Payment activity for processing payments.
 * This activity allows users to enter payment information such as email, card details, and address.
 */
public class Payment extends AppCompatActivity {
    private EditText name;
    private Button pay;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;
    private String amtToPay;
    private TextView toPayText;
    private String emailId;
    private EditText expiration;
    private EditText cvcET;
    private String card;
    private String username;
    private String concertName;
    private String orderCost;
    private String numberOfTickets;
    private String singleTicketCost;
    private ImageView back;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        EditText first = findViewById(R.id.firstFour);
        EditText second = findViewById(R.id.secondFour);
        EditText third = findViewById(R.id.thirdFour);
        EditText fourth = findViewById(R.id.lastFour);
        pay = findViewById(R.id.toDone);
        name = findViewById(R.id.fullName);
        expiration = findViewById(R.id.expiry);
        cvcET = findViewById(R.id.cvv);
        back = findViewById(R.id.back);

        String[] orderDetails = getIntent().getStringArrayExtra("Order Info");

        username = orderDetails[3];
        emailId = orderDetails[4];
        numberOfTickets = orderDetails[2];
        amtToPay = orderDetails[1];
        concertName = orderDetails[0];
        street1 = orderDetails[5];
        street2 = orderDetails[6];
        city = orderDetails[7];
        state = orderDetails[8];
        zip = orderDetails[9];

        String[] amtDollarRemovedArr = amtToPay.split("\\$");
        double toPayAmt = Double.parseDouble(amtDollarRemovedArr[1]);
        Log.i("Removed Dollar", String.valueOf(toPayAmt));

        toPayText = findViewById(R.id.amountToPay);
        toPayText.setText(amtToPay);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://coms-309-017.class.las.iastate.edu:8080/generateorder";
                String nameOnCard = name.getText().toString();
                String cvc = cvcET.getText().toString();
                String expiry = expiration.getText().toString();
                Map<String, Object> params = new HashMap<>();
                Map<String, String> paymentInfo = new HashMap<>();

                params.put("concertName",concertName);
                params.put("username",username);
                params.put("email",emailId);
                params.put("numberOfTickets",numberOfTickets);
                params.put("orderCost",toPayAmt);


                String firstFour = first.getText().toString();
                String secondFour = second.getText().toString();
                String thirdFour = third.getText().toString();
                String lastFour = fourth.getText().toString();
                if ((firstFour.isEmpty() || secondFour.isEmpty() || thirdFour.isEmpty() || lastFour.isEmpty())) {
                    first.setError("Card Number Missing");
                    second.setError("Card Number Missing");
                    third.setError("Card Number Missing");
                    fourth.setError("Card Number Missing");
                } else {
                    card = firstFour + secondFour + thirdFour + lastFour;
                    if (expiry.isEmpty()) {
                        expiration.setError("Missing Expiry Date");
                    } else if (cvc.isEmpty()) {
                        cvcET.setError("Missing CVC Code");
                    } else if (nameOnCard.isEmpty()) {
                        name.setError("Missing Name On Card");
                    } else {
                        if (card.length() == 16 && expiration.length() == 5 && (cvc.length() >= 3 && cvc.length() < 5) && !(nameOnCard.isEmpty()) && !(firstFour.isEmpty() && secondFour.isEmpty() && thirdFour.isEmpty() && lastFour.isEmpty())) {
                            paymentInfo.put("name",nameOnCard);
                            paymentInfo.put("cardNumber",card);
                            paymentInfo.put("expirationDate",expiry);
                            paymentInfo.put("streetOne",street1);
                            paymentInfo.put("streetTwo",street2);
                            paymentInfo.put("city",city);
                            paymentInfo.put("state",state);
                            paymentInfo.put("zipCode",zip);
                            params.put("paymentInfo",paymentInfo);
                            Log.i("PARAMS BODY REQUEST", String.valueOf(params));
                            JSONObject jsonObject = new JSONObject(params);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Volley Response", response.toString());
                                    try {
                                        String msg = response.getString("message");
                                        if (msg.equals("Order confirmation sent successfully")) {
                                            String orderNumber = response.getString("Order Number");
                                            Log.i("Order Number", orderNumber);
                                            Intent intent = new Intent(Payment.this, PaymentCompleteActivity.class);
                                            intent.putExtra("Order Number", orderNumber);
                                            intent.putExtra("Received", amtToPay);
                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("Volley Error", volleyError.toString());
                                }
                            });
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                        }
                    }
                }
            }
        });
    }
}

    /* onClick listener on login button pressed */
//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString();
//                String cardNum = cardNumEditText.getText().toString();
//                String expiration = expiryDateEditText.getText().toString();
//                String cvc = securityCodeEditText.getText().toString();
//                String cardHolderName = cardHolderNameEditText.getText().toString();
//                String street1 = street1EditText.getText().toString();
//                String street2 = street2EditText.getText().toString();
//                String city = cityEditText.getText().toString();
//                String state = stateCategory.toString();
//                String zipCode = zipcodeEditText.getText().toString();
//                String url = "http://coms-309-017.class.las.iastate.edu:8080/paymentinfo";
//                Map<String, String> params = new HashMap<>();
//                params.put("emailId",email);
//                params.put("cardNumber",cardNum);
//                params.put("expirationDate",expiration);
//                //params.put("cvc",cvc);
//                params.put("name",cardHolderName);
//                params.put("streetOne",street1);
//                params.put("streetTwo",street2);
//                params.put("city",city);
//                params.put("state",state);
//                params.put("zipCode",zipCode);
//
//                JSONObject obj = new JSONObject(params);
//
//                // Check if input values meet certain criteria before sending the payment request
//                if (cardNum.length() == 16 && expiration.length() == 5 && (cvc.length() >= 3 && cvc.length() < 5)){
//                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d("Volley Response", response.toString());
//                            // Parse the JSON array and add data to the adapter
//                            try {
//                                String message = response.getString("message");
//                                if (message.equals("success")) {
//                                    Class destination;
//                                    destination = PaymentCompleteActivity.class;
//                                    if (destination != null) {
//                                        Intent intent = new Intent(Payment.this, destination);
//                                        startActivity(intent);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e("Volley Error", error.toString());
//                                }
//                            }) {
//                        @Override
//                        public Map<String, String> getHeaders() {
//                            Map<String, String> headers = new HashMap<>();
//                            return headers;
//                        }
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            return params;
//                        }
//                    };
//
//                    // Adding request to request queue
//                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
//                }
//            }
//        });
    //}

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Set banner image based on the current theme mode when activity resumes
//        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
//            case Configuration.UI_MODE_NIGHT_YES:
//                banner.setImageResource(R.drawable.rifflight);
//                break;
//            case Configuration.UI_MODE_NIGHT_NO:
//                banner.setImageResource(R.drawable.riff);
//                break;
//        }
//    }