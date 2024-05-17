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

public class CreateGroupActivity extends AppCompatActivity {
    private EditText groupName;
    private EditText groupBio;
    private EditText passcode;
    private Button createGroup;
    private ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        createGroup = findViewById(R.id.createGroup);
        groupBio = findViewById(R.id.groupBio);
        groupName = findViewById(R.id.groupName);
        passcode = findViewById(R.id.passcode);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameGroup = groupName.getText().toString();
                String fourDigitPasscode = passcode.getText().toString();
                String bio = groupBio.getText().toString();
//                String createGroupURL = "http://coms-309-017.class.las.iastate.edu:8080/usergroups";
                String createGroupURL = "http://coms-309-017.class.las.iastate.edu:8080/usergroups";

                Map<String, Object> params = new HashMap<>();
                if(nameGroup.isEmpty()) {
                    groupName.setError("Group cannot be created without a name");
                } else {
                    params.put("name",nameGroup);
                    if(bio.isEmpty()) {
                        groupBio.setError("Group cannot be created without a bio");
                    } else {
                        params.put("groupBio",bio);
                        if(fourDigitPasscode.isEmpty()) {
                            passcode.setError("Group cannot be created without a passcode");
                        } else {
                            int fourDigit = Integer.parseInt(String.valueOf(fourDigitPasscode));
                            params.put("groupPassword",fourDigit);
                            JSONObject jsonObject = new JSONObject(params);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createGroupURL, jsonObject, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Message Returned",response.toString());
                                    String message = null;
                                    try {
                                        message = response.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(message.equals("success")) {
                                        Toast.makeText(CreateGroupActivity.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CreateGroupActivity.this, GroupsHomeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(CreateGroupActivity.this, "Group Not Created", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Volley Error", error.toString());
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
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                        }
                        }
                    }
                }
        });

    }
}
