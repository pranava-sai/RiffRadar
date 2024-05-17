package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity displays details about a concert.
 */
public class ConcertActivity extends AbstractElementActivity {
    private RecyclerView bandPreviewDisplay;
    private UserPreviewAdapter bandPreviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        childURL = Helper.baseURL + "/concerts";
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UserInfo.hasInstance()) {
            if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equals("venue")) {
                trashButton.setVisibility(View.VISIBLE);
            } else if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equals("attendee")) {
                favoriteButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void setUIListeners() {
        super.setUIListeners();
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("favorite", "clicked");
                favorite();
            }
        });

        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Helper.baseURL + "/venues/" + UserInfo.getInstance().loggedInUser.id + "/concerts/" + elementID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ConcertActivity.this, "Concert deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserProfile", "Error deleting concert", error);
                        // You might want to inform the user about the error
                        Toast.makeText(ConcertActivity.this, "Error deleting concert", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        bandPreviewAdapter.setOnClickListener(new UserPreviewAdapter.OnClickListener() {
            @Override
            public void onClick(User user) {
                Intent intent = new Intent(ConcertActivity.this, BandActivity.class);
                intent.putExtra("ID", user.id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initUI() {
        super.initUI();

        bandPreviewDisplay = findViewById(R.id.bandPreviews);
        bandPreviewAdapter = new UserPreviewAdapter(getLayoutInflater());
        bandPreviewDisplay.setAdapter(bandPreviewAdapter);
        bandPreviewDisplay.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void setDetails(JSONObject response) {
        super.setDetails(response);
        //bands
        try {
            if (response.has("bandsList")) {
                JSONArray bands = response.getJSONArray("bandsList");
                int length = bands.length();
                if (length > 0) {
                    bandPreviewAdapter.clearUsers();
                    bandsContainer.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < length; i++) {
                    addBand((JSONObject) bands.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOneDetail(response, "bands", bandsView, bandsContainer);

        //time
        setOneDetail(response, "time", timeView, timeContainer);

        //price
        setOneDetail(response, "price", priceView, priceContainer);

        //map
        try {
            if (response.has("venue")) {
                Log.d("Concert Location", "has venue");
                JSONObject venue = response.getJSONObject("venue");
                if (venue.has("address")) {
                    Log.d("Concert Location", "has address");
                    JSONObject address = venue.getJSONObject("address");
                    String state = address.getString("state");
                    if (!state.equals("null")) {
                        Log.d("Concert Location", "has state");
                        String city = address.getString("city");
                        if (!city.equals("null")) {
                            Log.d("Concert Location", "has city");
                            String streetAddress = address.getString("streetAddress");
                            if (!streetAddress.equals("null")) {
                                map.onCreate(null);
                                map.getMapAsync(this);
                                String text = streetAddress + "\n" + city + ", " + state;
                                Log.d("Concert Location", "has streetAddress: " + text);
                                addressView.setText(text);
                                setPos(streetAddress, city, state);

                                return;
                            }
                            map.onCreate(null);
                            map.getMapAsync(this);
                            addressView.setText(city + ", " + state);
                            setPos(city, state);

                            return;

                        }

                        map.onCreate(null);
                        map.getMapAsync(this);
                        addressView.setText(state);
                        setPos(state);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Error", "Set Details");
        }
    }

    protected void favorite() {
        if (favorite) {
            removeFromFavorites(currentObj);
        } else {
            addToFavorites(currentObj);
        }
    }

    private void addToFavorites(JSONObject obj) {

        if (!UserInfo.hasInstance()) {
            return;
        }

        String requestURL = Helper.baseURL + "/attendees/" + UserInfo.getInstance().loggedInUser.id + "/concerts/" + elementID;
        Log.d("Array URL", "createElement: " + requestURL);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.PUT, requestURL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", "Concert added: " + response.toString());
                favoriteButton.setImageResource(R.drawable.hearticontrue);
                favorite = !favorite;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "createList() ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void removeFromFavorites(JSONObject obj) {

        if (!UserInfo.hasInstance()) {
            return;
        }

        String requestURL = Helper.baseURL + "/attendees/" + UserInfo.getInstance().loggedInUser.id + "/concerts/" + elementID;
        Log.d("Array URL", "createElement: " + requestURL);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.DELETE, requestURL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", "Element response: " + response.toString());
                favoriteButton.setImageResource(R.drawable.hearticon);
                favorite = !favorite;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "createList() ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void checkForFavorites() {
        if (!UserInfo.hasInstance()) {
            return;
        }

        String requestURL = Helper.baseURL + "/attendees/" + UserInfo.getInstance().loggedInUser.id;
        Log.d("Check for Favorites", "req: " + requestURL);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("favorites", response.toString());
                try {
                    if (response.has("favorites")) {
                        JSONArray favorites = response.getJSONArray("favorites");
                        int length = favorites.length();
                        for (int i = 0; i < length; i++) {
                            if (favorites.getJSONObject(i).toString().equals(currentObj.toString())) {
                                Log.d("CheckForFavs", "found" + i);
                                favorite = true;
                                favoriteButton.setImageResource(R.drawable.hearticontrue);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "createList() ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    @Override
    protected void createElement(int id) {
        String requestURL = childURL + "/" + id;
        Log.d("Array URL", "createElement: " + requestURL);

        if (id == -1) {
            setInvisible();
            return;
        }

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", "Element response: " + response.toString());
                currentObj = response;
                /* check if already is favorited */
                checkForFavorites();
                // attempt to add attributes
                setDetails(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "createList() ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void addBand(JSONObject bandObj) {
        Gson gson = new Gson();
        Band band = gson.fromJson(bandObj.toString(), Band.class);
        bandPreviewAdapter.addUser(band);
    }
}