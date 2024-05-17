package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInfoActivity extends AppCompatActivity {
    private TextView groupName;
    private TextView groupBio;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> userList;
    private String groupId;
    private String bio;
    private String nameOfGroup;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info);

        groupName = findViewById(R.id.groupName); // Assuming the ID of TextView for group name is group_name
        groupBio = findViewById(R.id.groupBio); // Assuming the ID of TextView for group bio is group_bio
        back = findViewById(R.id.backArrow);
        // Get the group id from the previous screen
        groupId = getIntent().getStringExtra("ID");
        Log.i("group id",groupId);
        nameOfGroup = getIntent().getStringExtra("Name");
        Log.i("group name",nameOfGroup);
        groupName.setText(nameOfGroup);
        listView = findViewById(R.id.list);
        userList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, GroupChatActivity.class);
                intent.putExtra("Name",nameOfGroup);
                intent.putExtra("ID",groupId);
                startActivity(intent);
            }
        });
        String urlGroupInfo = "http://coms-309-017.class.las.iastate.edu:8080/usergroups/" + groupId;
        Log.i("URL",urlGroupInfo);

        // Request a JSONObject response from the provided URL for group information
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGroupInfo, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Group Bio",response.toString());
                try {
                    bio = response.getString("groupBio");
                    groupBio.setText(bio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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

        String urlAttendees = "http://coms-309-017.class.las.iastate.edu:8080/usergroups/" + groupId + "/attendees";
        Log.i("GROUP ID", String.valueOf(groupId));
        Log.i("URL ATTENDEES IN GROUP", urlAttendees);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, urlAttendees, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("RESPONSE", response.toString());
                        try {
                            userList.clear(); // Clear existing data
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                String userName = user.getString("name");
                                Log.i("Username", userName);
                                userList.add(userName);
                            }
                            arrayAdapter.notifyDataSetChanged(); // Notify adapter
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

// Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET, urlAttendees, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Parse the JSON response
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject user = response.getJSONObject(i);
//                                String userName = user.getString("name");
//                                userList.add(userName);
//                            }
//                            // Notify the adapter of the data change
//                            arrayAdapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        // Add the request to the RequestQueue
//        queue.add(jsonArrayRequest);
    }
}
