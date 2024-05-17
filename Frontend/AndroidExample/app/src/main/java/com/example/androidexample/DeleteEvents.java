package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * This class provides functionality to delete events from the server.
 */
public class DeleteEvents extends AppCompatActivity {
    // UI elements
    private Button deleteConcert;
    private EditText id;

    /**
     * Initializes the activity.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_events);

        // Initialize UI elements
        deleteConcert = findViewById(R.id.deleteConcert);
        id = findViewById(R.id.concertID);

        // Set click listener for delete button
        deleteConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct URL for DELETE request
                String url = Helper.baseURL + "/concerts/" + id.getText().toString();
                // Create JsonObjectRequest for DELETE method
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Show success message on successful response
                        Toast.makeText(DeleteEvents.this, "Concert Deleted", Toast.LENGTH_SHORT).show();
                        // Redirect to AdminScreen activity
                        Intent intent = new Intent(DeleteEvents.this, AdminScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    // Handle errors if any
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                // Add the request to the VolleySingleton request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });
    }
}