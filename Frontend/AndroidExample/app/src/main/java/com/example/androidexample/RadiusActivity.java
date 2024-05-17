package com.example.androidexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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

import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows a user to change their location.
 */
public class RadiusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView backButton;
    private SeekBar seekBar;
    private EditText setLocation;
    private View searchButton;
    private TextView distanceDisplay;
    private View confirmButton;
    private MapView map;
    private GoogleMap currentMap;

    private double lat;
    private double lng;
    private LatLng position = new LatLng(42.02525, -93.64830);
    private FusedLocationProviderClient locationClient;
    private int radius = 15;
    private float level;
    int min, max; // min and max of seek bar

    private static final String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";
    private static int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_radius);             // link to Concert activity XML

        initUI();

        setUIListeners();

        checkSaved();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        map = (MapView) findViewById(R.id.map);
        map.onCreate(mapViewBundle);

        map.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    private void checkSaved(){
        if (LocationInfo.hasInstance()){
            if ((LocationInfo.getInstance().position.latLng != null) || (LocationInfo.getInstance().position.radius != 0)){
                if (LocationInfo.getInstance().position != null){
                    position = LocationInfo.getInstance().position.latLng;
                    Log.d("hasInstance", "pos: " + position.toString());
                }

                if (LocationInfo.getInstance().position.radius != 0){
                    radius = (int) (LocationInfo.getInstance().position.radius / 1.60934) + min;
                    Log.d("hasInstance", "radius: " + radius);
                    seekBar.setProgress(radius - min);
                }
            }
        }
    }

    private void initUI(){
        /* initialize UI elements */
        backButton = findViewById(R.id.backButton);      // link to greeter
        seekBar = findViewById(R.id.seekBar);
        distanceDisplay = findViewById(R.id.distanceDisplay);
        confirmButton = findViewById(R.id.confirmButton);
        setLocation = findViewById(R.id.enterLocation);
        searchButton = findViewById(R.id.searchButton);

        /* set up seek bar */
        max = 100;
        min = 1;
        seekBar.setMax(max - min);

        //map
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        position = new LatLng(0, 0);
        radius = seekBar.getProgress() + min;
    }

    private void setUIListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(RadiusActivity.this);

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                Helper.hideKeyboard(RadiusActivity.this);

                //set location
                String address = String.valueOf(setLocation.getText());
                setLocation.setText("");
                addressToCoordinates(address);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceDisplay.setText(progress + min + " miles");
                radius = progress + min;

                if (currentMap != null){
                    setZoomLevel(progress + min);
                }

                Log.d("Changed", "prog: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CONNECT", position.toString());
                Log.d("CONNECT", "rad: " + radius);

                LocationInfo.getInstance().position.latLng = position;
                LocationInfo.getInstance().position.radius = (int)(radius * 1.60934); // store radius as km
                finish();
            }
        });
    }

    /**
     * If there is previously saved info, load depending on that info.
     * @param outState Bundle in which to place your saved state.
     */
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
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    /**
     * Required by Google Maps API. Declares what to do when the map is returned.
     * @param newMap : the returned map
     */
    @Override
    public void onMapReady(GoogleMap newMap) {
        // Disable user gestures such as zooming and panning
        newMap.getUiSettings().setAllGesturesEnabled(false);

        // set currentMap to newMap
        currentMap = newMap;

        // Set initial camera position
        if (position == null || (position.latitude == 0 && position.longitude == 0)){
            position = new LatLng(42.02525, -93.64830);
        }
        setZoomLevel(radius);
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

    /**
     * Required by interface.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    private void setZoomLevel(int distanceInMiles) {
        // 1 mile is approximately 1609.34 meters
        double meters = distanceInMiles * 1609.34;

        //add circle
        currentMap.clear();
        currentMap.addCircle(new CircleOptions().center(position).fillColor(Color.parseColor("#55808080")).strokeColor(Color.parseColor("#55000000")).strokeWidth(6).radius(meters));

        //increase meters (padding)
        meters *= 1.6;

        // zoom level formula provided by Google Maps API
        level = (float) (16 - Math.log(meters / 500) / Math.log(2));
        currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, level));

    }

    private void addressToCoordinates(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += address;
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")) {
                        JSONObject location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        lat = location.getDouble("lat");
                        lng = location.getDouble("lng");
                        position = new LatLng(lat, lng);

                        setZoomLevel(radius);
                    } else {
                        Log.d("addressToCoords", "onResponse: Status not OK");
                    }
                } catch (Exception e) {
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