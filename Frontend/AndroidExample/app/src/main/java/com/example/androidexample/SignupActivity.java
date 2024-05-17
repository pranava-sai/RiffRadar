package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private TextView otp;
    private int OTP;
    private int generatedOTP;
    private EditText OTPEntry;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;

    private EditText addressEditText;
    private EditText descriptionEditText;
    private EditText capacityEditText;
    private EditText genreEditText;
    private LinearLayout addressLayout;
    private LinearLayout descriptionLayout;
    private LinearLayout capacityLayout;
    private LinearLayout genreLayout;
    private Button signupButton;
    private Button sendOTP;
    private Button resendOTP;

    private Spinner userType;
    private String userCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmEditText = findViewById(R.id.confirm_password);
        userType = findViewById(R.id.spinner7);
        signupButton = findViewById(R.id.signup_button);
        addressLayout = findViewById(R.id.addressLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        capacityLayout = findViewById(R.id.capacityLayout);
        genreLayout = findViewById(R.id.genreLayout);
        addressEditText = findViewById(R.id.addressEdt);
        descriptionEditText = findViewById(R.id.descriptionEdt);
        capacityEditText = findViewById(R.id.capacityEdt);
        genreEditText = findViewById(R.id.genreEdt);
        sendOTP = findViewById(R.id.sendOTP);
        otp = findViewById(R.id.OTPView);
        OTPEntry = findViewById(R.id.otp);
        resendOTP = findViewById(R.id.resendOTP);

        otp.setVisibility(View.GONE);
        OTPEntry.setVisibility(View.GONE);
        signupButton.setVisibility(View.GONE);

        String[] userArray = {"Select Category", "Attendee", "Band", "Venue"};
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userArray);
        userType.setAdapter(userAdapter);

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userCategory = parent.getItemAtPosition(position).toString();
                handleVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            private void handleVisibility() {
                if (userCategory.equals("Band")) {
                    addressLayout.setVisibility(View.VISIBLE);
                    descriptionLayout.setVisibility(View.VISIBLE);
                    capacityLayout.setVisibility(View.GONE);
                    genreLayout.setVisibility(View.VISIBLE);
                } else if (userCategory.equals("Venue")) {
                    addressLayout.setVisibility(View.VISIBLE);
                    descriptionLayout.setVisibility(View.VISIBLE);
                    capacityLayout.setVisibility(View.VISIBLE);
                    genreLayout.setVisibility(View.GONE);
                } else {
                    addressLayout.setVisibility(View.GONE);
                    descriptionLayout.setVisibility(View.GONE);
                    capacityLayout.setVisibility(View.GONE);
                    genreLayout.setVisibility(View.GONE);
                }
            }
        });

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String id = "0";
                String request = "signup";

                Map<String, Object> params = new HashMap<>();
                String otpURL = "http://coms-309-017.class.las.iastate.edu:8080/generate-otp";
                params.put("emailId",email);
                params.put("name",name);
                params.put("request",request);
                params.put("id",id);

                JSONObject jsonObject1 = new JSONObject(params);
                JsonObjectRequest jor1 = new JsonObjectRequest(Request.Method.POST, otpURL, jsonObject1, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("OTP",response.toString());
                        try {
                            String otpMessage = response.getString("message");
                            generatedOTP = response.getInt("OTP Code");
                            if(otpMessage.equals("OTP sent successfully")) {
                                otp.setVisibility(View.VISIBLE);
                                OTPEntry.setVisibility(View.VISIBLE);
                                signupButton.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        return headers;
                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jor1);
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpResendURL = "http://coms-309-017.class.las.iastate.edu:8080/generate-otp";
                String emailLinkedWithAccount = emailEditText.getText().toString();
                String request = "signup";
                String name = nameEditText.getText().toString();
                String id = "0";

                Map<String, Object> params = new HashMap<>();
                if (emailLinkedWithAccount != null && !emailLinkedWithAccount.isEmpty()) {
                    params.put("emailId", emailLinkedWithAccount);
                    params.put("request",request);
                    params.put("name",name);
                    params.put("id",id);

                    JSONObject jsonObject = new JSONObject(params);
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, otpResendURL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response2) {
                            Log.d("Volley Response 2", response2.toString());
                            try {
                                String otpMessage2 = response2.getString("message");
                                generatedOTP = response2.getInt("OTP Code");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR","User Not Found");
                            Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            return headers;
                        }

                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jor);
                } else {
                    Toast.makeText(getApplicationContext(), "OTP not sent", Toast.LENGTH_LONG).show();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOTP = OTPEntry.getText().toString();
                if (Integer.valueOf(enteredOTP) == generatedOTP) {
                    Toast.makeText(SignupActivity.this, "OTP Validation Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    Toast.makeText(SignupActivity.this, "Signup Successful. You may now login", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String confirm = confirmEditText.getText().toString();
                    String url = "http://coms-309-017.class.las.iastate.edu:8080/" + userCategory.toLowerCase() + "s";
                    String name = nameEditText.getText().toString();
                    String capacity = capacityEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String genre = genreEditText.getText().toString();
                    String address = addressEditText.getText().toString();

                    Map<String, Object> params = new HashMap<>();
                    params.put("name", name);
                    Map<String, String> loginInfo = new HashMap<>();
                    if (isValidEmail(email)) {
                        loginInfo.put("emailId", email);
                    } else {
                        emailEditText.setError("Enter a valid email address");
                        return;
                    }

                    if ((password.length() >= 8 && confirm.length() >= 8) && password.equals(confirm)) {
                        if (isPasswordValid(password)) {
                            loginInfo.put("password", password);
                        } else {
                            passwordEditText.setError("Password must be at least 8 characters long and contain a number and a special character");
                            confirmEditText.setError("Password must be at least 8 characters long and contain a number and a special character");
                            return;
                        }
                    } else {
                        passwordEditText.setError("Password must be at least 8 characters long");
                        confirmEditText.setError("Password must be at least 8 characters long");
                        Toast.makeText(getApplicationContext(), "Password is supposed to be at least 8 characters", Toast.LENGTH_LONG).show();
                        return;
                    }

                    loginInfo.put("userType", userCategory.toLowerCase());
                    params.put("loginInfo", loginInfo);
                    params.put("description", description);

                    String[] addressSplit = address.split(", ");
                    Map<String, Object> params2 = new HashMap<>();

                    if (addressSplit.length == 4) {
                        params2.put("streetAddress", addressSplit[0]);
                        params2.put("city", addressSplit[1]);
                        params2.put("state", addressSplit[2]);
                        params2.put("zipcode", addressSplit[3]);
                        params.put("address",params2);
                        if (userCategory.equals("Band")) {
                            params.put("genre", genre);
                        } else if (userCategory.equals("Venue")) {
                            params.put("capacity", capacity);
                        }
                    } else {
                        addressEditText.setError("Invalid address. Try again");
                    }

                    JSONObject obj = new JSONObject(params);

                    if (password.length() >= 8 || confirm.length() >= 8) {
                        if (password.equals(confirm) && !userCategory.equals("Select Category")) {
                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Volley Response", response.toString());
                                    Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Volley Error", error.toString());
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

                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
                        } else {
                            Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Signup Not Successful", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "OTP Validation Unsuccessful. Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPasswordValid(String password) {
        boolean containsNumber = false;
        boolean containsSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                containsSpecialChar = true;
            }

            if (containsNumber && containsSpecialChar) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}