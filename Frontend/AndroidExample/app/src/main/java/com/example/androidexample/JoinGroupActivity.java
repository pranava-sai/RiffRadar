package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class JoinGroupActivity extends AppCompatActivity {
    private EditText groupName;
    private EditText passcode;
    private Button join;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);
        groupName = findViewById(R.id.nameGroup);
        passcode = findViewById(R.id.passwordToEnter);
        join = findViewById(R.id.joinGroup);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                int attendeeID = UserInfo.getInstance().loggedInUser.id;
                String fourDigitPasscode = passcode.getText().toString();
                int fourDigit = Integer.parseInt(fourDigitPasscode);
                String joinGroupURL = "http://coms-309-017.class.las.iastate.edu:8080/usergroups/"+name+"/attendees/"+attendeeID+"/password/"+fourDigit;
                //String joinGroupURL = "http://10.0.2.2:8080/usergroups/"+name+"/attendees/"+attendeeID+"/password/"+fourDigit;
                Toast.makeText(JoinGroupActivity.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(JoinGroupActivity.this, GroupsHomeActivity.class);
//                startActivity(intent);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, joinGroupURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE",response.toString());
                        String message = null;
                        try {
                            message = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(message.equals("success")) {
                            Intent intent = new Intent(JoinGroupActivity.this, GroupsHomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.toString());
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
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}