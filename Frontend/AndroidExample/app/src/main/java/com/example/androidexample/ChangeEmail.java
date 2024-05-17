package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Activity to handle changing user email address.
 */
public class ChangeEmail extends AppCompatActivity {

    private EditText oldEmail;
    private EditText newEmail;

    private EditText confirmEmail;
    private Button updateEmail;

    private ImageView back;
    /**
     * Initializes the activity layout and sets up views.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);

        oldEmail = findViewById(R.id.oldEmail);
        newEmail = findViewById(R.id.newEmail);
        confirmEmail = findViewById(R.id.confirmEmail);
        updateEmail = findViewById(R.id.changeEmail);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String old = oldEmail.getText().toString();
                String newEmailAddress = newEmail.getText().toString();
                String confirmEmailAddress = confirmEmail.getText().toString();
                int id = UserInfo.getInstance().loggedInUser.loginInfo.id;
                String id2 = String.valueOf(id);
                String password = UserInfo.getInstance().loggedInUser.loginInfo.password;
                String userType = UserInfo.getInstance().loggedInUser.loginInfo.userType;
                String url = "http://coms-309-017.class.las.iastate.edu:8080/logininfo/"+id;
                // Create request parameters
                Map<String, String> params = new HashMap<>();
                if(newEmailAddress.equals(confirmEmailAddress)) {
                    params.put("id",id2);
                    params.put("emailId",newEmailAddress);
                    params.put("password",password);
                    params.put("userType",userType);
                }

                JSONObject obj = new JSONObject(params);
                // Create JsonObjectRequest for making PUT request
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Log.d("Volley Response", response.toString());
                        if(old.equals(newEmailAddress)) {
                            // Notify user that old and new emails cannot be the same
                            Toast.makeText(ChangeEmail.this, "Old and New Email Cannot be same", Toast.LENGTH_SHORT).show();
                        } else {
                            if(newEmailAddress.equals(confirmEmailAddress)) {
                                // Notify user that email was updated successfully
                                Toast.makeText(ChangeEmail.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                                // Determine destination activity based on user type
                                Class destination;
                                switch (userType) {
                                    case "attendee":
                                        destination = GuestFeedActivity.class;
                                        break;
                                    case "band":
                                        destination = BandFeedActivity.class;
                                        break;
                                    case "venue":
                                        destination = VenueFeedActivity.class;
                                        break;
                                    default: destination = null;
                                }
                                // Start destination activity if it's not null
                                if (destination != null) {
                                    Intent intent = new Intent(ChangeEmail.this, destination);
                                    startActivity(intent);
                                }
                            } else {
                                // Notify user that email and confirm email do not match
                                Toast.makeText(ChangeEmail.this, "Email do not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("Volley Error", "Error in changing email: " + error.toString());

                        // Notify user about error
                        Toast.makeText(ChangeEmail.this, "Error updating email", Toast.LENGTH_SHORT).show();
                    }
                });
                // Add request to the RequestQueue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
