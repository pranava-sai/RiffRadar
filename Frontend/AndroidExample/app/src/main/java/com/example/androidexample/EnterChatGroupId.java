package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class EnterChatGroupId extends AppCompatActivity {
    private EditText groupName;
    String username;
    String groupID;
    String name;
    private Button start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_group_num);

        username = UserInfo.getInstance().loggedInUser.name;
        groupName = findViewById(R.id.nameGroup);
        start = findViewById(R.id.startChat);

        String[] nameSplit = username.split(" ");
        if(nameSplit.length > 2 || nameSplit.length < 2) {
            name = nameSplit[0];
        } else if (nameSplit.length == 2) {
            name = nameSplit[1];
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfGroup = groupName.getText().toString();
                Log.d("Group Name",nameOfGroup);
                //String url = "http://coms-309-017.class.las.iastate.edu:8080/getusergroupid/"+nameOfGroup;
                String url = "http://coms-309-017.class.las.iastate.edu:8080/getusergroupid/"+nameOfGroup;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Group ID", response.toString());
                        try {
                            groupID = response.getString("groupId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(EnterChatGroupId.this, GroupChatActivity.class);
                        intent.putExtra("Name",nameOfGroup);
                        intent.putExtra("ID",groupID);
                        startActivity(intent);
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

                // Adding request to request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
