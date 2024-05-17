package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * First screen a user sees. Allows them to choose how to access rest of the app.
 */
public class GreeterActivity extends AppCompatActivity {

    private Button loginButton;     // define login button variable
    private Button signupButton;
    private TextView guestButton, fanButton, bandButton, venueButton, adminButton;
    private LinearLayout shortcutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_greeter);             // link to Main activity XML

        /* initialize UI elements */
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);// link to signup button in the Main activity XML
        //profile = findViewById(R.id.main_profile_btn);
        guestButton = findViewById(R.id.guestButton);
        fanButton = findViewById(R.id.fanButton);
        bandButton = findViewById(R.id.bandButton);
        venueButton = findViewById(R.id.hostButton);
        adminButton = findViewById(R.id.adminButton);
        shortcutContainer = findViewById(R.id.typeContainer2);
        shortcutContainer.setVisibility(View.GONE);


        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(GreeterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(GreeterActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on guest button pressed */
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreeterActivity.this, GuestFeedActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on fan button pressed */
        fanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 7;
                String url = Helper.baseURL + "/attendee/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Attendee.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(GreeterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GreeterActivity.this, FanFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GreeterActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        /* click listener on venue button pressed */
        venueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 12;
                String url = Helper.baseURL + "/venue/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Venue.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(GreeterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GreeterActivity.this, VenueFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GreeterActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        /* click listener on band button pressed */
        bandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 13;
                String url = Helper.baseURL + "/band/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Band.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(GreeterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GreeterActivity.this, BandFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GreeterActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreeterActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}