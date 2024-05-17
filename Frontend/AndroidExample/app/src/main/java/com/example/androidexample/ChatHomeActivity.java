package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Opens a web socket.
 * Allows a user to pick who they will message.
 */
public class ChatHomeActivity extends AppCompatActivity{

    private ImageView backButton;

    private SearchView searchView;
    private ListView previewDisplay;
    private CaseInsensitiveArrayAdapter previewAdapter;

    private ArrayList<User> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);

        /* initialize UI elements */
        initUI();

        /* init chats */
        chats = new ArrayList<User>();
        getChatPreviews();

        /* connect listeners */
        setListeners();
    }

    private void initUI(){
        backButton = findViewById(R.id.backButton);

        searchView = findViewById(R.id.search);
        previewDisplay = findViewById(R.id.previews);
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                previewAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                previewAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    //new connect
    private void connect(int id, String name){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("name", name);

        if (UserInfo.hasInstance()){
            if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equals("band")){
                intent.putExtra("source", "b" + UserInfo.getInstance().loggedInUser.id);
                intent.putExtra("dest", "v" + id);
            } else if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equals("venue")) {
                intent.putExtra("source", "v" + UserInfo.getInstance().loggedInUser.id);
                intent.putExtra("dest", "b" + id);
            } else {
                Toast.makeText(this, "Error connecting to chat", Toast.LENGTH_LONG).show();
            }

            // go to chat activity
            startActivity(intent);
        } else {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    private void getChatPreviews(){
        if (UserInfo.hasInstance()){
            String url = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase() + "s/" + UserInfo.getInstance().loggedInUser.id;
            JsonObjectRequest getProfile = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("getChatPreviews()", "response: " + response.toString());

                    chats.clear();

                    try {
                        if (UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase().equals("band")){
                            if (response.has("venues")){
                                JSONArray venues = response.getJSONArray("venues");
                                int length = venues.length();
                                for (int i = 0; i < length; i++){
                                    Gson gson = new Gson();
                                    Venue venue = gson.fromJson(venues.getJSONObject(i).toString(), Venue.class);
                                    chats.add(venue);
                                    Log.d("getChatPreviews()", "added: " + venue.toString());

                                    previewAdapter = new CaseInsensitiveArrayAdapter(ChatHomeActivity.this, R.layout.chat_preview, chats);
                                    previewDisplay.setAdapter(previewAdapter);

                                    previewAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int position) {
                                            connect(previewAdapter.getItem(position).id, previewAdapter.getItem(position).name);
                                        }
                                    });

                                    Helper.setListViewSize(previewDisplay);
                                }

                            }
                        } else if (UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase().equals("venue")){
                            if (response.has("bands")){
                                JSONArray bands = response.getJSONArray("bands");
                                int length = bands.length();
                                for (int i = 0; i < length; i++){
                                    Gson gson = new Gson();
                                    Band band = gson.fromJson(bands.getJSONObject(i).toString(), Band.class);
                                    chats.add(band);
                                    Log.d("getChatPreviews()", "added: " + band.toString());

                                    previewAdapter = new CaseInsensitiveArrayAdapter(ChatHomeActivity.this, R.layout.chat_preview, chats);
                                    previewDisplay.setAdapter(previewAdapter);

                                    previewAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(int position) {
                                            connect(previewAdapter.getItem(position).id, previewAdapter.getItem(position).name);
                                        }
                                    });

                                    Helper.setListViewSize(previewDisplay);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", "error: " + error.toString());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                    return params;
                }
            };
            Log.d("GET", "autofill: " + getProfile.toString());

            // Adding request to request queue
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(getProfile);
        }
    }
}
