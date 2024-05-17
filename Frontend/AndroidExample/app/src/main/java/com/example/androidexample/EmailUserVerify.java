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
 * This class is for the
 */
public class EmailUserVerify extends AppCompatActivity {
    // UI Elements
    private EditText emailEdt;
    private Button sendOTP;
    private ImageView back;

    /**
     * Initializes the activity's user interface and sets up event listeners for buttons.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_email_input_user);

        // Initializing the UI elements
        emailEdt = findViewById(R.id.email);
        sendOTP = findViewById(R.id.sendOTP);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString();
                String url = Helper.baseURL + "/logininfo";
                String emailLinkedWithAccount = UserInfo.getInstance().loggedInUser.loginInfo.emailId;
                String currentPassword = UserInfo.getInstance().loggedInUser.loginInfo.password;
                String userCategory = UserInfo.getInstance().loggedInUser.loginInfo.userType;
                String id = String.valueOf(UserInfo.getInstance().loggedInUser.loginInfo.id);

                Map<String, Object> params = new HashMap<>();
                Log.i("Email Entered",email);
                Log.i("Email Linked",emailLinkedWithAccount);
                Log.i("Password",currentPassword);
                Log.i("Category",userCategory);
                Log.i("ID",id);
                Log.i("Email",String.valueOf(email.equals(emailLinkedWithAccount)));
                if (email.equals(emailLinkedWithAccount)) {
                    params.put("emailId", email);
                    params.put("password",currentPassword);
                    params.put("usertype",userCategory);
                    params.put("id",id);

                    JSONObject jsonObject = new JSONObject(params);
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            // Parse the JSON array and add data to the adapter
                            try {
                                String message = response.getString("message");
                                String request = "reset-password";
                                String name = UserInfo.getInstance().loggedInUser.name;
                                Log.i("Name",name);
                                Log.i("Request",request);
                                Log.i("Message",message);
                                if (message.equals("success")) {
                                    String otpURL = "http://coms-309-017.class.las.iastate.edu:8080/generate-otp";
                                    Map<String, Object> param = new HashMap<>();
                                    param.put("emailId",email);
                                    param.put("request",request);
                                    param.put("name",name);
                                    param.put("id",id);

                                    JSONObject jsonObject1 = new JSONObject(param);
                                    JsonObjectRequest jor1 = new JsonObjectRequest(Request.Method.POST, otpURL, jsonObject1, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.i("OTP",response.toString());
                                            try {
                                                String otpMessage = response.getString("message");
                                                int OTP = response.getInt("OTP Code");
                                                if(otpMessage.equals("OTP sent successfully")) {
                                                    Class destination;
                                                    destination = OTP_Reset_Password.class;
                                                    if (destination != null) {
                                                        Intent intent = new Intent(EmailUserVerify.this, destination);
                                                        intent.putExtra("OTP",OTP);
                                                        startActivity(intent);
                                                    }
                                                }
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
                    Toast.makeText(getApplicationContext(), "User Not Found. OTP not sent", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
