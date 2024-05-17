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
 * An activity for deleting payment information.
 * This activity allows an administrator to delete payment information by sending a DELETE request to the server.
 */
public class DeletePayInfo extends AppCompatActivity {

    // UI elements
    private Button update;
    private EditText id;

    /**
     * Called when the activity is starting.
     * Initializes UI components and sets up a click listener for the delete button.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_crucial);

        // Initialize UI components
        update = findViewById(R.id.deletePayInfo);
        id = findViewById(R.id.payID);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct URL for DELETE request
                String url = "http://coms-309-017.class.las.iastate.edu:8080/paymentinfo/"+id.getText().toString();
                // Create a JsonObjectRequest for sending DELETE request
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display success message
                        Toast.makeText(DeletePayInfo.this, "Payment Information Deleted", Toast.LENGTH_SHORT).show();
                        // Redirect to AdminScreen activity after deletion
                        Intent intent = new Intent(DeletePayInfo.this, AdminScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response (if any)

                    }
                });
                // Add the request to the Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });
    }
}