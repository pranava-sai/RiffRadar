package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * DeleteUsers Activity allows the admin to delete users from the system.
 * It provides a Spinner to select the user type and an EditText to input the user ID.
 * Upon clicking the delete button, it sends a DELETE request to the server to delete the user.
 * If successful, it displays a toast message indicating the user deletion and redirects to the AdminScreen.
 * If there is an error in the request, it handles it appropriately.
 */
public class DeleteUsers extends AppCompatActivity {
    // UI elements
    private Spinner userType;

    private Button delete;
    private EditText id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_users);
        // Initialize UI elements
        userType = findViewById(R.id.spinner7);
        delete = findViewById(R.id.deleteUser);
        id = findViewById(R.id.userID);

        // Populate Spinner with user types
        String [] userArray = {"Select Category", "Attendee", "Band", "Venue"};
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userArray);
        userType.setAdapter(userAdapter);

        // Set OnClickListener for delete button
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct URL for DELETE request
                String url = Helper.baseURL + "/" + userType.getSelectedItem().toString().toLowerCase()+"s/" + id.getText().toString();
                String loginURL = Helper.baseURL + "/" + id.getText().toString();
                // Create DELETE request using Volley
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On successful deletion, display toast message and redirect to AdminScreen
                        //Toast.makeText(DeleteUsers.this, "User Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DeleteUsers.this, AdminScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });
                // Add request to Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);

                JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.DELETE, loginURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On successful deletion, display toast message and redirect to AdminScreen
                        Toast.makeText(DeleteUsers.this, "User Deleted Everywhere", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DeleteUsers.this, AdminScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });
                // Add request to Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq2);
            }
        });
    }
}