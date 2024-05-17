package com.example.androidexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RadiusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton logoutButton;  // define username textview variable
    private ImageView banner;
    private SeekBar seekBar;
    private EditText setLocation;
    private View searchButton;
    private TextView distanceDisplay;
    private View confirmButton;
    private MapView map;
    private GoogleMap currentMap;

    private String address;
    private double lat;
    private double lng;
    private LatLng position;
    private float level;

    private static final String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);             // link to Concert activity XML

        /* initialize UI elements */
        logoutButton = findViewById(R.id.main_logout_btn);      // link to greeter
        banner = findViewById(R.id.banner);
        seekBar = findViewById(R.id.seekBar);
        distanceDisplay = findViewById(R.id.distanceDisplay);
        confirmButton = findViewById(R.id.confirmButton);
        setLocation = findViewById(R.id.enterLocation);
        searchButton = findViewById(R.id.searchButton);

        /* set up seek bar */
        int max = 100;
        int min = 1;
        seekBar.setMax(max - min);

        //map
        position = new LatLng(0, 0);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        map = (MapView) findViewById(R.id.map);
        map.onCreate(mapViewBundle);

        map.getMapAsync(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = String.valueOf(setLocation.getText());
                setLocation.setText("");
                addressToCoordinates(address);
                hideKeyboard(RadiusActivity.this);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceDisplay.setText(progress + 1 + " miles");
                setZoomLevel(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when login button is pressed, use intent to switch to Greeter Activity */
                Intent intent = new Intent(RadiusActivity.this, GuestFeedActivity.class);
                startActivity(intent);
            }
        });
        setLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){ //enter key code
                    //hide keyboard
                    hideKeyboard(RadiusActivity.this);

                    //set location
                    String address = String.valueOf(setLocation.getText());
                    setLocation.setText("");
                    addressToCoordinates(address);

                    //exit
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onMapReady(GoogleMap newMap) {
        // Disable user gestures such as zooming and panning
        newMap.getUiSettings().setAllGesturesEnabled(false);

        // set currentMap to newMap
        currentMap = newMap;

        // Set initial camera position
        position = new LatLng(42.02525, -93.64830);
        setZoomLevel(15);
    }

    @Override
    protected void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    private void setZoomLevel(int distanceInMiles) {
        // 1 mile is approximately 1609.34 meters
        double meters = distanceInMiles * 1609.34;
        // zoom level formula provided by Google Maps API
        level = (float) (16 - Math.log(meters / 500) / Math.log(2));
        currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, level));

    }

    void setImageURL(String url, ImageView imageView){
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }

    public static void hideKeyboard(Activity activity) {
        //get input method
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //get the current focus
        View view = activity.getCurrentFocus();

        //create a new window if there is none
        if (view == null) {
            view = new View(activity);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    void addressToCoordinates(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += address;
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")){
                        JSONObject location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        lat = location.getDouble("lat");
                        lng = location.getDouble("lng");
                        position = new LatLng(lat, lng);
                        currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, level));
                    } else {
                        Log.d("addressToCoords", "onResponse: Status not OK");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
}