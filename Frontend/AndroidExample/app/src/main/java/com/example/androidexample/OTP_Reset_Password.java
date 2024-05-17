package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class validates the OTP the user has received on their email
 */
public class OTP_Reset_Password extends AppCompatActivity {
    // UI Elements
    private EditText OTP;
    private Button validate;
    private Button resend;
    int generatedOTP;
    String OTPEntered;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_reset_password);

        OTP = findViewById(R.id.otp);
        validate = findViewById(R.id.validateOTP);
        resend = findViewById(R.id.resendOTP);
        back = findViewById(R.id.back);

        generatedOTP = getIntent().getIntExtra("OTP",0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBack = new Intent(OTP_Reset_Password.this, EmailUserVerify.class);
                startActivity(iBack);
            }
        });
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTPEntered = OTP.getText().toString();
                if (Integer.valueOf(OTPEntered) == generatedOTP) {
                    Toast.makeText(OTP_Reset_Password.this, "OTP Validation Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OTP_Reset_Password.this, ResetPassword.class);
                    Toast.makeText(OTP_Reset_Password.this, "You may now reset your password", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(OTP_Reset_Password.this, "OTP Validation Unsuccessful. Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://coms-309-017.class.las.iastate.edu:8080/logininfo";
                String emailLinkedWithAccount = UserInfo.getInstance().loggedInUser.loginInfo.emailId;
                String currentPassword = UserInfo.getInstance().loggedInUser.loginInfo.password;
                String userCategory = UserInfo.getInstance().loggedInUser.loginInfo.userType;
                String id = String.valueOf(UserInfo.getInstance().loggedInUser.loginInfo.id);

                Map<String, Object> params = new HashMap<>();
                if (emailLinkedWithAccount != null && !emailLinkedWithAccount.isEmpty()) {
                    params.put("emailId", emailLinkedWithAccount);
                    params.put("password",currentPassword);
                    params.put("usertype",userCategory);
                    params.put("id",id);

                    JSONObject jsonObject = new JSONObject(params);
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            String request = "reset-password";
                            String name = UserInfo.getInstance().loggedInUser.name;
                            // Parse the JSON array and add data to the adapter
                            try {
                                String message = response.getString("message");
                                if (message.equals("success")) {
                                    String otpURL = "http://coms-309-017.class.las.iastate.edu:8080/generate-otp";
                                    Map<String, Object> param = new HashMap<>();
                                    param.put("emailId",emailLinkedWithAccount);
                                    param.put("request",request);
                                    param.put("name",name);
                                    param.put("id",id);

                                    JSONObject jsonObject1 = new JSONObject(param);
                                    JsonObjectRequest jor1 = new JsonObjectRequest(Request.Method.POST, otpURL, jsonObject1, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String otpMessage = response.getString("message");
                                                generatedOTP = response.getInt("OTP Code");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

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
                    // Error message for password match - negative
                    Toast.makeText(getApplicationContext(), "OTP not sent", Toast.LENGTH_LONG).show();
                }
            };
        });
    }
}
