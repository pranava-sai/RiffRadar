package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to handle password reset functionality for the user.
 */
public class ResetPassword extends AppCompatActivity {
    // UI elements
    private EditText oldPass;
    private EditText newPass;
    private EditText confirmPass;
    private Button reset;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // Initialize UI elements
        oldPass = findViewById(R.id.oldPassword);
        newPass = findViewById(R.id.newPassword);
        back = findViewById(R.id.back);
        confirmPass = findViewById(R.id.confirmNewPassword);

        reset = findViewById(R.id.reset_password_button);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set click listener for reset button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = oldPass.getText().toString();
                String newPassword = newPass.getText().toString();
                String confirmPassword = confirmPass.getText().toString();
                int id = UserInfo.getInstance().loggedInUser.loginInfo.id;
                String id2 = String.valueOf(id);
                String email = UserInfo.getInstance().loggedInUser.loginInfo.emailId;
                String userType = UserInfo.getInstance().loggedInUser.loginInfo.userType;
                // Construct the URL with the endpoint for PUT request
                String url = "http://coms-309-017.class.las.iastate.edu:8080/logininfo/"+id;
                // Prepare parameters for PUT request
                Map<String, String> params = new HashMap<>();
                if(newPassword.equals(confirmPassword)) {
                    params.put("id",id2);
                    params.put("emailId",email);
                    params.put("password",newPassword);
                    params.put("userType",userType);
                }
                JSONObject obj = new JSONObject(params);
                // Create JSON request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        // Handle response
                        if(old.equals(newPassword)) {
                            Toast.makeText(ResetPassword.this, "Old and New Password cannot be the same", Toast.LENGTH_SHORT).show();
                        } else {
                            if(newPassword.equals(confirmPassword)) {
                                Toast.makeText(ResetPassword.this, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                                finish();
                                Class destination;
                                switch (userType) {
                                    case "attendee":
                                        destination = FanProfileActivity.class;
                                        break;
                                    case "band":
                                        destination = BandProfileActivity.class;
                                        break;
                                    case "venue":
                                        destination = VenueProfileActivity.class;
                                        break;
                                    default:
                                        destination = null;
                                }
                                Intent intent = new Intent(ResetPassword.this, destination);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ResetPassword.this, "Passwords don't match. Please Retry!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("Volley Error", "Error in resetting password: " + error.toString());

                        // You can display an error message or handle it accordingly
                        Toast.makeText(ResetPassword.this, "Error in resetting password", Toast.LENGTH_SHORT).show();
                    }
                });
                // Add request to the queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}