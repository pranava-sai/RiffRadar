package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView individualPricing;
    private TextView quantityPricing;
    private TextView quantity;
    private double taxPercentages;
    private ConstraintLayout layout;
    private TextView increase;
    private TextView decrease;
    private String concertName;
    private String pricing;
    private double itemizedCost;
    private TextView subtotalPricing;
    private TextView handlingCharges;
    private TextView taxAmt;
    private TextView totalPricing;
    private double itemsCost;
    private TextView error;
    private String quantities;
    private double cost;
    private int currentQuantity;
    private String street1;
    private EditText getStreet1;
    private String street2;
    private EditText getStreet2;
    private String city;
    private EditText getCity;
    private String zip;
    private EditText getZip;
    private Spinner stateSpinner;
    private String stateSelected;
    private double taxPercentage;
    private double tax;
    private double handlingPrice;
    private double totalToPay;
    private Button checkout;
    private Button cancel;
    private ImageView delete;
    private ImageView back;
    private EditText first;
    private EditText last;
    private EditText email;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize TextViews
        itemName = findViewById(R.id.titleTxt);
        individualPricing = findViewById(R.id.priceEachItem);
        quantity = findViewById(R.id.numberItemTxt);
        increase = findViewById(R.id.addPlus);
        decrease = findViewById(R.id.minus);
        quantityPricing = findViewById(R.id.itemPrice);
        error = findViewById(R.id.errorTxt);
        first = findViewById(R.id.firstName);
        last = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        error.setVisibility(View.GONE);
        getStreet1 = findViewById(R.id.street1);
        getStreet2 = findViewById(R.id.street2);
        getCity = findViewById(R.id.city);
        getZip = findViewById(R.id.zip);

        subtotalPricing = findViewById(R.id.subtotalTxt);
        handlingCharges = findViewById(R.id.handlingTxt);
        taxAmt = findViewById(R.id.taxTxt);
        totalPricing = findViewById(R.id.totalTxt);
        stateSpinner = findViewById(R.id.state_edt);
        handlingPrice = 2.5;
        checkout = findViewById(R.id.checkout);
        cancel = findViewById(R.id.cancel);
        delete = findViewById(R.id.deleteItem);
        back = findViewById(R.id.backArrow);

        //taxAmt.setText("$0.0");

        final String[] strVal = {"0","0","0","0"};
        int id = UserInfo.getInstance().loggedInUser.id;
        final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("Message Returned", jsonObject.toString());
                String message = jsonObject.toString();
                if (!message.isEmpty()) {
                    strVal[0] = "1";
                    try {
                        String nameOfConcert = jsonObject.getString("concertName");
                        String numTickets = jsonObject.getString("numberOfTickets");
                        String pricePerTicket = jsonObject.getString("pricePerTicket");
                        strVal[1] = nameOfConcert;
                        strVal[2] = numTickets;
                        strVal[3] = pricePerTicket;
                        itemName.setText(strVal[1]);
                        quantity.setText(strVal[2]);
                        quantityPricing.setText("$" + strVal[3]);
                        individualPricing.setText("$" + strVal[3]);
                        subtotalPricing.setText("$" + strVal[3]);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    strVal[0] = "0";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = UserInfo.getInstance().loggedInUser.id;
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
                Log.i("URL",url);

                Map<String, String> params = new HashMap<>();
                Log.i("Name",strVal[1]);
                Log.i("num",String.valueOf(currentQuantity));
                Log.i("Single Ticket Price",strVal[3]);
                params.put("concertName",strVal[1]);
                if(currentQuantity == 0) {
                    params.put("numberOfTickets",strVal[2]);
                } else {
                    params.put("numberOfTickets",String.valueOf(currentQuantity));
                }

                params.put("pricePerTicket",strVal[3]);
                JSONObject jsonObject = new JSONObject(params);
                JsonObjectRequest jorAdd = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("SUCCESS",jsonObject.toString());
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley Response Error","Error detected while PUTTING "+volleyError.toString());
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorAdd);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = first.getText().toString() + " " + last.getText().toString();
                street1 = getStreet1.getText().toString();
                street2 = getStreet2.getText().toString();
                city = getCity.getText().toString();
                zip = getZip.getText().toString();
                String currentTotal = totalPricing.getText().toString();
                String emailId = email.getText().toString();
                Intent iBill = new Intent(CartActivity.this, Payment.class);
                String numTicketsNow = quantity.getText().toString();
                String[] orderInfo = {strVal[1], currentTotal, numTicketsNow ,username, emailId, street1, street2, city, stateSelected, zip};
                iBill.putExtra("Order Info",orderInfo);
                startActivity(iBill);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = UserInfo.getInstance().loggedInUser.id;
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
                JsonObjectRequest jorCancelSetNull = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(CartActivity.this, "Cart Cleared", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CartActivity.this, GuestFeedActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error","Error HERE line 159 " + volleyError.toString());
                        volleyError.printStackTrace();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorCancelSetNull);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = UserInfo.getInstance().loggedInUser.id;
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
                Log.i("Delete URL",url);
                JsonObjectRequest jorDeleteSetNull = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(CartActivity.this, "Cart Deleted", Toast.LENGTH_SHORT).show();
                        try {
                            String message = jsonObject.getString("message");
                            if(message.equals("success")) {
                                Intent intent = new Intent(CartActivity.this, GuestFeedActivity.class);
                                startActivity(intent);
                            } else {
                                Log.i("Failed","Cart Not Set to Null");
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error","Error HERE line 193 " + volleyError.toString());
                        volleyError.printStackTrace();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorDeleteSetNull);
            }
        });



        String [] statesArray = {"Select State","AK","AL", "AR", "AS", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI",
                "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY"};
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statesArray);
        stateSpinner.setAdapter(userAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Log.i("State Selected",stateSelected);

                //taxPercentages = handleStateTax(stateSelected);
                double taxAmount;
                int currentQuantity = Integer.parseInt(quantity.getText().toString());
                switch (stateSelected) {
                    case "AL":
                    case "GA":
                    case "HI":
                    case "NY":
                    case "WY":
                        taxAmount = 0.04 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Alabama (AL)
                        break;
                    case "AR":
                    case "KS":
                    case "WA":
                        taxAmount = 0.065 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Arkansas (AR)
                        break;
                    case "AZ":
                        taxAmount = 0.056 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Arizona (AZ)
                        break;
                    case "CA":
                        taxAmount = 0.0725 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // California (CA)
                        break;
                    case "CO":
                        taxAmount = 0.029 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Colorado (CO)
                        break;
                    case "CT":
                        taxAmount = 0.0635 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Connecticut (CT)
                        break;
                    case "FL":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // Florida (FL)
                        break;
                    case "IA":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Iowa (IA)
                        break;
                    case "ID":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // Idaho (ID)
                        break;
                    case "IL":
                        taxAmount = 0.0625 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Illinois (IL)
                        break;
                    case "IN":
                        taxAmount = 0.07 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Indiana (IN)
                        break;
                    case "KY":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Kentucky (KY)
                        break;
                    case "LA":
                        taxAmount = 0.0445 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Louisiana (LA)
                        break;
                    case "MA":
                        taxAmount = 0.0625 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Massachusetts (MA)
                        break;
                    case "MD":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // Maryland (MD)
                        break;
                    case "ME":
                        taxAmount = 0.055 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Maine (ME)
                        break;
                    case "MI":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Michigan (MI)
                        break;
                    case "MN":
                        taxAmount = 0.0688 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Minnesota (MN)
                        break;
                    case "MO":
                        taxAmount = 0.0423 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Missouri (MO)
                        break;
                    case "MP":
                        taxAmount = 0.055 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Northern Mariana Islands (MP)
                        break;
                    case "MS":
                        taxAmount = 0.07 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Mississippi (MS)
                        break;
                    case "NC":
                        taxAmount = 0.0475 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // North Carolina (NC)
                        break;
                    case "ND":
                        taxAmount = 0.05 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // North Dakota (ND)
                        break;
                    case "NE":
                        taxAmount = 0.055 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Nebraska (NE)
                        break;
                    case "NJ":
                        taxAmount = 0.0663 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // New Jersey (NJ)
                        break;
                    case "NM":
                        taxAmount = 0.0513 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // New Mexico (NM)
                        break;
                    case "NV":
                        taxAmount = 0.0685 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Nevada (NV)
                        break;
                    case "OH":
                        taxAmount = 0.0575 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // Ohio (OH)
                        break;
                    case "OK":
                        taxAmount = 0.045 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Oklahoma (OK)
                        break;
                    case "PA":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);; // Pennsylvania (PA)
                        break;
                    case "PR":
                        taxAmount = 0.0105 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Puerto Rico (PR)
                        break;
                    case "RI":
                        taxAmount = 0.07 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Rhode Island (RI)
                        break;
                    case "SC":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // South Carolina (SC)
                        break;
                    case "SD":
                        taxAmount = 0.045 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // South Dakota (SD)
                        break;
                    case "TN":
                        taxAmount = 0.07 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Tennessee (TN)
                        break;
                    case "TX":
                        taxAmount = 0.0625 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Texas (TX
                        break;
                    case "UT":
                        taxAmount = 0.0595 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Utah (UT)
                        break;
                    case "VA":
                        taxAmount = 0.053 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Virginia (VA)
                        break;
                    case "VT":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Vermont (VT)
                        break;
                    case "WI":
                        taxAmount = 0.05 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // Wisconsin (WI)
                        break;
                    case "WV":
                        taxAmount = 0.06 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50); // West Virginia (WV)
                        break;
                    default:
                        taxAmount = 0.0 * currentQuantity * (Double.parseDouble(strVal[3]) + 2.50);
                        break;
                }
                taxAmt.setText(String.format("$%.2f",taxAmount));
                double sub = Double.parseDouble(strVal[3]);
                double cumulativeTotal = 2.50 + taxAmount + sub;
                totalPricing.setText(String.format("$%.2f",cumulativeTotal));
                Log.i("Tax for state",stateSelected + "-------" + taxPercentages);
                Log.i("Tax Amt for state based on the quantity of tickets",stateSelected + "----- "+String.format("$%.2f",taxAmount));
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

        handlingCharges.setText("$2.50");
        // Get data passed from previous activity

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantity.getText().toString());
                if (currentQuantity < 10) { // Check if quantity is less than the maximum limit (10)
                    currentQuantity++; // Increment the quantity
                    quantity.setText(String.valueOf(currentQuantity)); // Update the quantity TextView
                    //double total = updatePriceDetails(currentQuantity);
                    double updatedQPrice = currentQuantity * Integer.parseInt(strVal[3]);
                    quantityPricing.setText(String.valueOf("$" + updatedQPrice));
                    subtotalPricing.setText(String.valueOf("$" + updatedQPrice));

                    taxPercentages = handleStateTax(stateSelected);
                    Log.i("Tax Percentage",stateSelected + "--" + String.valueOf(taxPercentages));
                    double taxForOrder = taxPercentages * (updatedQPrice + 2.50);
                    String formatTax = String.format("%.2f",taxForOrder);
                    taxAmt.setText("$"+formatTax);
                    Log.i("Tax For Order",stateSelected + "--" + String.valueOf(taxForOrder));
                    double totalToPayToday = updatedQPrice + 2.50 + taxForOrder;
                    String formatTotal = String.format("%.2f",totalToPayToday);
                    taxAmt.setText("$"+formatTax);
                    Log.i("Order Cost",stateSelected + "--" + String.valueOf(totalToPayToday));
                    totalPricing.setText(String.valueOf("$"+formatTotal));
                } else {
                    // Notify the user that the maximum limit (10 items) has been reached
                    error.setVisibility(View.VISIBLE);
                    Toast.makeText(CartActivity.this, "Maximum limit reached (10 items)", Toast.LENGTH_SHORT).show();
                }

                // Hide error if quantity is 1 or more
                if (currentQuantity >= 1) {
                    error.setVisibility(View.GONE);
                }
                int id = UserInfo.getInstance().loggedInUser.id;
                String singlePrice = strVal[3];
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
//                final String url = "http://10.0.2.2:8080/carts/"+username;

                Map<String, String> params = new HashMap<>();
                params.put("concertName",strVal[1]);
                params.put("numberOfTickets",String.valueOf(currentQuantity));
                params.put("pricePerTicket",singlePrice);
                JSONObject jsonObject = new JSONObject(params);
                JsonObjectRequest jorAdd = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("SUCCESS",jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley Response Error","Error detected while PUTTING "+volleyError.toString());
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorAdd);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantity.getText().toString());
                if (currentQuantity >= 1) { // Check if quantity is more than 1
                    currentQuantity--; // Decrement the quantity
                    quantity.setText(String.valueOf(currentQuantity)); // Update the quantity TextView
                    double updatedQPrice = currentQuantity * Integer.parseInt(strVal[3]);
                    quantityPricing.setText(String.valueOf("$" + updatedQPrice));
                    subtotalPricing.setText(String.valueOf("$" + updatedQPrice));

                    taxPercentages = handleStateTax(stateSelected);
                    Log.i("Tax Percentage",stateSelected + "--" + String.valueOf(taxPercentages));
                    double taxForOrder = taxPercentages * (updatedQPrice + 2.50);
                    String formatTax = String.format("%.2f",taxForOrder);
                    taxAmt.setText("$"+formatTax);
                    Log.i("Tax For Order",stateSelected + "--" + String.valueOf(taxForOrder));
                    double totalToPayToday = updatedQPrice + 2.50 + taxForOrder;
                    String formatTotal = String.format("%.2f",totalToPayToday);
                    taxAmt.setText("$"+formatTax);
                    Log.i("Order Cost",stateSelected + "--" + String.valueOf(totalToPayToday));
                    totalPricing.setText(String.valueOf("$"+formatTotal));
//                    double itemsCostMinus = cost * currentQuantity;
//                    quantityPricing.setText(String.format("$%.2f", itemsCostMinus));
//                    subtotalPricing.setText(String.format("$%.2f", itemsCostMinus));
//                    if(!(stateSelected.equals("Select State"))) {
//                        tax = itemsCostMinus * taxPercentage;
//                        taxAmt.setText(String.format("$%.2f", tax));
//                        totalToPay = itemsCostMinus + handlingPrice + tax;
//                        totalPricing.setText(String.format("$%.2f", totalToPay));
                    //}
                } else if (currentQuantity < 1) {
                    // Hide error if quantity is increased back to 1
                    error.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(CartActivity.this, GuestFeedActivity.class);
                    startActivity(intent);
                    Toast.makeText(CartActivity.this, "Item deleted. Cart is now empty", Toast.LENGTH_SHORT).show();
                }
                int id = UserInfo.getInstance().loggedInUser.id;
                String singlePrice = strVal[3];
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;

                Map<String, String> params = new HashMap<>();
                params.put("concertName",strVal[1]);
                params.put("numberOfTickets",String.valueOf(currentQuantity));
                params.put("pricePerTicket",singlePrice);
                JSONObject jsonObject = new JSONObject(params);
                JsonObjectRequest jorMinus = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("SUCCESS",jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley Response Error","Error detected while PUTTING "+volleyError.toString());
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorMinus);
            }
        });

//        increase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentQuantity = Integer.parseInt(quantity.getText().toString());
//
//                if (currentQuantity < 10) { // Check if quantity is less than the maximum limit (10)
//                    currentQuantity++; // Increment the quantity
//                    quantity.setText(String.valueOf(currentQuantity)); // Update the quantity TextView
//
//                    double itemsCostAdd = cost * currentQuantity;
//                    quantityPricing.setText(String.format("$%.2f", itemsCostAdd));
//                } else {
//                    // Notify the user that the maximum limit (10 items) has been reached
//                    if (currentQuantity > 10) {
//                        error.setVisibility(View.VISIBLE);
//                    } else{
//                        error.setVisibility(View.GONE);
//                    }
//                    Toast.makeText(CartActivity.this, "Maximum limit reached (10 items)", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        decrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentQuantity = Integer.parseInt(quantity.getText().toString());
//
//                if (currentQuantity > 1) { // Check if quantity is more than 1
//                    currentQuantity--; // Decrement the quantity
//                    quantity.setText(String.valueOf(currentQuantity)); // Update the quantity TextView
//
//                    double itemsCostMinus = cost * currentQuantity;
//                    quantityPricing.setText(String.format("$%.2f", itemsCostMinus));
//                } else {
//                    if(currentQuantity < 1) {
//                        error.setVisibility(View.VISIBLE);
//                    } else {
//                        error.setVisibility(View.GONE);
//                    }
//
//                    // Show delete confirmation dialog when quantity is 1
//                    //quantity.setError("Are you sure you want to delete this?");
//                    quantity.setText("0"); // Set quantity to zero or remove item
//                    quantityPricing.setText("$0.00");
//                    Toast.makeText(CartActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

//    private double updatePriceDetails(int quantity) {
//        double itemsCost = cost * quantity;
//        quantityPricing.setText(String.format("$%.2f", itemsCost));
//        subtotalPricing.setText(String.format("$%.2f", itemsCost));
//
//        if (!(stateSelected.equals("Select State"))) {
//            tax = itemsCost * taxPercentage;
//            taxAmt.setText(String.format("$%.2f", tax));
//            if(tax != 0) {
//                totalToPay = itemsCost + handlingPrice + tax;
//            } else {
//                totalToPay = itemsCost + handlingPrice;
//            }
//            totalPricing.setText(String.format("$%.2f", totalToPay));
//        }
//
//        return totalToPay;
//    }

    private double handleStateTax(String selectedState) {
        switch (selectedState) {
            case "AL":
            case "GA":
            case "HI":
            case "NY":
            case "WY":
                taxPercentage = 0.04; // Alabama (AL)
                break;
            case "AR":
            case "KS":
            case "WA":
                taxPercentage = 0.065; // Arkansas (AR)
                break;
            case "AZ":
                taxPercentage = 0.056; // Arizona (AZ)
                break;
            case "CA":
                taxPercentage = 0.0725; // California (CA)
                break;
            case "CO":
                taxPercentage = 0.029; // Colorado (CO)
                break;
            case "CT":
                taxPercentage = 0.0635; // Connecticut (CT)
                break;
            case "FL":
                taxPercentage = 0.06; // Florida (FL)
                break;
            case "IA":
                taxPercentage = 0.06; // Iowa (IA)
                break;
            case "ID":
                taxPercentage = 0.06; // Idaho (ID)
                break;
            case "IL":
                taxPercentage = 0.0625; // Illinois (IL)
                break;
            case "IN":
                taxPercentage = 0.07; // Indiana (IN)
                break;
            case "KY":
                taxPercentage = 0.06; // Kentucky (KY)
                break;
            case "LA":
                taxPercentage = 0.0445; // Louisiana (LA)
                break;
            case "MA":
                taxPercentage = 0.0625; // Massachusetts (MA)
                break;
            case "MD":
                taxPercentage = 0.06; // Maryland (MD)
                break;
            case "ME":
                taxPercentage = 0.055; // Maine (ME)
                break;
            case "MI":
                taxPercentage = 0.06; // Michigan (MI)
                break;
            case "MN":
                taxPercentage = 0.0688; // Minnesota (MN)
                break;
            case "MO":
                taxPercentage = 0.0423; // Missouri (MO)
                break;
            case "MP":
                taxPercentage = 0.055; // Northern Mariana Islands (MP)
                break;
            case "MS":
                taxPercentage = 0.07; // Mississippi (MS)
                break;
            case "NC":
                taxPercentage = 0.0475; // North Carolina (NC)
                break;
            case "ND":
                taxPercentage = 0.05; // North Dakota (ND)
                break;
            case "NE":
                taxPercentage = 0.055; // Nebraska (NE)
                break;
            case "NJ":
                taxPercentage = 0.0663; // New Jersey (NJ)
                break;
            case "NM":
                taxPercentage = 0.0513; // New Mexico (NM)
                break;
            case "NV":
                taxPercentage = 0.0685; // Nevada (NV)
                break;
            case "OH":
                taxPercentage = 0.0575; // Ohio (OH)
                break;
            case "OK":
                taxPercentage = 0.045; // Oklahoma (OK)
                break;
            case "PA":
                taxPercentage = 0.06; // Pennsylvania (PA)
                break;
            case "PR":
                taxPercentage = 0.105; // Puerto Rico (PR)
                break;
            case "RI":
                taxPercentage = 0.07; // Rhode Island (RI)
                break;
            case "SC":
                taxPercentage = 0.06; // South Carolina (SC)
                break;
            case "SD":
                taxPercentage = 0.045; // South Dakota (SD)
                break;
            case "TN":
                taxPercentage = 0.07; // Tennessee (TN)
                break;
            case "TX":
                taxPercentage = 0.0625; // Texas (TX
                break;
            case "UT":
                taxPercentage = 0.0595; // Utah (UT)
                break;
            case "VA":
                taxPercentage = 0.053; // Virginia (VA)
                break;
            case "VT":
                taxPercentage = 0.06; // Vermont (VT)
                break;
            case "WI":
                taxPercentage = 0.05; // Wisconsin (WI)
                break;
            case "WV":
                taxPercentage = 0.06; // West Virginia (WV)
                break;
            default:
                taxPercentage = 0.00;
                break;
        }

        return taxPercentage;
//        // Update tax and total pricing if both pricing and state are selected
//        if (pricing != null && !stateSelected.equals("Select State")) {
//            cost = Double.parseDouble(pricing); // Parse pricing string to double
//            individualPricing.setText(String.format("$%.2f", cost));
//            quantityPricing.setText(String.format("$%.2f", cost));
//            subtotalPricing.setText(String.format("$%.2f", cost));
//
//            tax = cost * taxPercentage;
//            taxAmt.setText(String.format("$%.2f", tax));
//            if(tax != 0) {
//                totalToPay = cost + handlingPrice + tax;
//            } else {
//                totalToPay = cost + handlingPrice;
//            }
//            totalPricing.setText(String.format("$%.2f", totalToPay));
//        } else {
//            // If state is "Select State" or pricing is null, reset tax and total pricing
//            taxAmt.setText("$0.00");
//            if(tax != 0) {
//                totalToPay = cost + handlingPrice + tax;
//            } else {
//                totalToPay = cost + handlingPrice;
//            }
//            totalPricing.setText(String.format("$%.2f", totalToPay)); // Set total to cost only
//        }
    }
//
//    private String[] getExistingCart() {
//
//        return strVal;
//    }
}
