package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * LoginActivity - This activity handles the login process for users.
 *
 * <p>Upon launching, it presents a layout with fields for email and password input, along with a
 * login button. When the login button is pressed, it validates the user's input, sends a login
 * request to the server, and processes the response accordingly. If the login is successful, it
 * redirects the user to their respective feed activity based on their user type.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;  // define email edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);            // link to Login activity XML

        /* initialize UI elements */
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);    // link to login button in the Login activity XML// link to signup button in the Login activity XML

        /* onClick listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String url = "http://coms-309-017.class.las.iastate.edu:8080/login";
                Map<String, String> params = new HashMap<>();
                if (isValidEmail(email)) {
                    params.put("emailId", email);
                } else {
                    emailEditText.setError("Enter a valid email address");
                    Toast.makeText(LoginActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing if email is not valid
                }

                if (password.length() >= 8 && isPasswordValid(password)) {
                    params.put("password", password);
                } else {
                    passwordEditText.setError("Password must be at least 8 characters long and contain a number and a special character");
                    Toast.makeText(LoginActivity.this, "Password must be at least 8 characters long and contain a number and a special character", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing if password is not valid
                }

                JSONObject obj = new JSONObject(params);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        // Parse the JSON array and add data to the adapter
                        try {
                            Gson gson = new Gson();
                            int id = response.getInt("id");
                            String userType = response.getString("userType");
                            String url2 = null;
                            if (userType.equals("admin")) {
                                String email = emailEditText.getText().toString();
                                if (email.equals("msspranavasai@gmail.com")) {
                                    url2 = "http://coms-309-017.class.las.iastate.edu:8080/admins/3";
                                } else if (email.equals("kadenb4u@gmail.com")) {
                                    url2 = "http://coms-309-017.class.las.iastate.edu:8080/admins/1";
                                }
                            } else {
                                url2 = "http://coms-309-017.class.las.iastate.edu:8080/"+userType.toLowerCase()+"/"+id;
                            }

                            //url2 and perform a get request
                            JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Password Length", "Length: " + password.length());
                                    try {
                                        // gson is Google's JSON Library - converts JSON to String etc. Java Library
                                        Class destination;
                                        User loggedInUser;
                                        switch (userType) {
                                            case "attendee":
                                                destination = FanFeedActivity.class;
                                                loggedInUser = gson.fromJson(response.toString(), Attendee.class);
                                                break;
                                            case "band":
                                                destination = BandFeedActivity.class;
                                                loggedInUser = gson.fromJson(response.toString(), Band.class);
                                                break;
                                            case "venue":
                                                destination = VenueFeedActivity.class;
                                                loggedInUser = gson.fromJson(response.toString(), Venue.class);
                                                break;
                                            case "admin":
                                                destination = AdminHomeActivity.class;
                                                loggedInUser = gson.fromJson(response.toString(), Admin.class);
                                                break;
                                            default:
                                                loggedInUser = null;
                                                destination = null;
                                        }
                                        if (loggedInUser != null) {
                                            UserInfo.getInstance().loggedInUser = loggedInUser;
                                        }
                                        if (destination != null) {
                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, destination);
                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(LoginActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq2);
                            Log.d("Login req", jsonObjReq2.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley Error", "This is the error ---" + error.toString());
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
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

            }
        });


    }

    /**
     * Checks if the given email is valid.
     *
     * @param email The email to validate.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Checks if the given password meets the specified criteria.
     *
     * @param password The password to check.
     * @return True if the password meets the criteria, false otherwise.
     */
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
}