package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
public class FavoritesActivity extends AppCompatActivity{

    private ImageView backButton;


    private RecyclerView cardDisplay;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }

    @Override
    protected void onResume(){
        super.onResume();
        /* init chats */
        getFavorites();

        /* initialize UI elements */
        initUI();

        /* connect listeners */
        setListeners();

        /* scroll to top */
        cardDisplay.scrollToPosition(0);
    }

    private void initUI(){
        backButton = findViewById(R.id.backButton);

        cardDisplay = findViewById(R.id.cards);
        cardAdapter = new CardAdapter(getLayoutInflater(), this);
        cardDisplay.setAdapter(cardAdapter);
        cardDisplay.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardAdapter.setOnClickListener(new CardAdapter.OnClickListener() {
            @Override
            public void onClick(int id, String url) {
                Log.d("Listener", "Card Clicked");
                if (UserInfo.hasInstance()){
                    Class<? extends AbstractElementActivity> destination;
                    String type = UserInfo.getInstance().loggedInUser.loginInfo.userType;

                    if (type.equals("attendee")){
                        destination = ConcertActivity.class;
                    } else if (type.equals("band")){
                        destination = VenueActivity.class;
                    } else if (type.equals("venue")) {
                        destination = BandActivity.class;
                    } else {
                        return;
                    }
                    try {
                        Intent intent = new Intent(FavoritesActivity.this, destination);
                        intent.putExtra("ID", id);
                        Log.d("fav", "starting");
                        startActivity(intent);
                        Log.d("fav", "started");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getFavorites(){
        if (UserInfo.hasInstance()){
            String url = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase() + "s/" + UserInfo.getInstance().loggedInUser.id;
            JsonObjectRequest getProfile = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley Response", "response: " + response.toString());

                    try {
                        if (response.has("favorites")){
                            cardAdapter.clearCards();

                            JSONArray favorites = response.getJSONArray("favorites");
                            int length = favorites.length();

                            for (int i = 0; i < length; i++){
                                cardAdapter.addCard(favorites.getJSONObject(i));
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
