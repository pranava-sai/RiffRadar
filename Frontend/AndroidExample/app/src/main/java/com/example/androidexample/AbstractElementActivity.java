package com.example.androidexample;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A template for all the element activities.
 * Contains shared behavior like UI elements and parsing information.
 */
public abstract class AbstractElementActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView backButton;
    protected ImageView chatButton;
    protected ImageView favoriteButton;
    protected ImageView trashButton;
    protected TextView nameView, descriptionView, genreView, agesView, addressView, priceView, bandsView, timeView, capacityView;
    private ImageView imageView;
    private Button feedback;
    private Button review;
    Button addToCart;
    protected LinearLayout agesContainer, genreContainer, mapContainer, priceContainer, bandsContainer, timeContainer, capacityContainer, spotifyContainer;
    protected MapView map;
    protected ConstraintLayout buttonContainer;
    protected ImageView directionsButton;
    private Intent intent;
    protected JSONObject currentObj;
    protected boolean favorite = false;
    private GoogleMap currentMap;
    private LatLng position;

    private Boolean showMap = false;

    private static final String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";
    protected String childURL;
    protected int elementID;
    private int concert_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);// link to Band activity XML
        concert_id = getIntent().getIntExtra("ConcertID",-1);
        Log.i("ID Received from Card", String.valueOf(concert_id));
        /**
         * Initializes the buttons and sets their click listeners.
         */
//        feedback = findViewById(R.id.rateTheEvent);
//        review = findViewById(R.id.reviews);
//        addToCart = findViewById(R.id.addToCart);
        /* initialize UI elements */
        initUI();

        // Set onClickListener for the 'feedback' button
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iFeedback = new Intent(AbstractElementActivity.this, ConcertRating.class);
//                startActivity(iFeedback);
//            }
//        });

        // Set onClickListener for the 'reviews' button
//        review.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("Reviews Button", "Clicked");
//                Intent intent = new Intent(AbstractElementActivity.this, ReviewActivity.class);
//                Log.i("Element ID Sending to Reviews", String.valueOf(concert_id));  // Ensure this is getting logged
//                intent.putExtra("WSsessionID", concert_id);
//                startActivity(intent);
//            }
//        });



        // Set onClickListener for the 'payment' button
//        addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AbstractElementActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
//                int id = UserInfo.getInstance().loggedInUser.id;
//                Log.i("ID", String.valueOf(id));
//                String singlePrice = priceView.getText().toString();
//                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
//                Log.i("URL",url);
//                String concertName = nameView.getText().toString();
//
//                String[] splitPrice = singlePrice.split("\\$");
//                Map<String, String> params = new HashMap<>();
//                Log.i("Name",concertName);
//                Log.i("num","1");
//                Log.i("Single Ticket Price",splitPrice[1]);
//                params.put("concertName",concertName);
//                params.put("numberOfTickets","1");
//                params.put("pricePerTicket",splitPrice[1]);
//
//                JSONObject jsonObject = new JSONObject(params);
//                JsonObjectRequest jorAdd = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        Log.i("SUCCESS",jsonObject.toString());
//                        Intent iAddToCart = new Intent(AbstractElementActivity.this, CartActivity.class);
//                        startActivity(iAddToCart);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Log.e("Volley Response Error","Error detected while PUTTING "+volleyError.toString());
//                    }
//                });
//                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorAdd);
//            }
//        });
        /* initialize UI elements */
        initUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (showMap){
            Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
            if (mapViewBundle == null) {
                mapViewBundle = new Bundle();
                outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
            }
            map.onSaveInstanceState(mapViewBundle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showMap){
            map.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* default to invisible */
        setInvisible();

        /* get intent */
        intent = getIntent();

        /* set details*/
        elementID = intent.getIntExtra("ID", -1);
        createElement(concert_id);

        /* set listeners */
        setUIListeners();

        /* show map if present*/
        if (showMap){
            map.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (showMap) {
            map.onStop();
        }
    }

    /**
     * A method used with the google maps API. When the request for the map has returned, this method is called.
     * @param newMap : the map to be implemented
     */
    @Override
    public void onMapReady(GoogleMap newMap) {
        // Disable user gestures such as zooming and panning
        newMap.getUiSettings().setAllGesturesEnabled(false);

        // set currentMap to newMap
        currentMap = newMap;

        // Set initial camera position
        setZoomLevel(10);
    }

    @Override
    protected void onPause() {
        if (showMap) {
            map.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (showMap) {
            map.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (showMap) {
            map.onLowMemory();
        }
    }

    protected void setPos(String streetAddress, String city, String state) {
        Log.d("setPos", "3");
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + streetAddress  + "+" + city + "+" + state);
                Log.d("directions 3", gmmIntentUri.toString());
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + streetAddress  + "+" + city + "+" + state + "&dirflg=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += (streetAddress + "+"  + city + "+" + state);
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        Log.d("MAP URL", "addressToCoordinates: " + url);
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")){
                        JSONObject location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        String lat = location.getString("lat");
                        String lng = location.getString("lng");
                        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        mapContainer.setVisibility(View.VISIBLE);
                        setZoomLevel(10);
                    } else {
                        setPos(city, state);
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
    protected void setPos(String city, String state) {
        Log.d("setPos", "2");
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + city + "+" + state);
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + city + "+" + state + "&dirflg=d");
                Log.d("directions 2", gmmIntentUri.toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += (city + state);
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        Log.d("MAP URL", "addressToCoordinates: " + url);
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")){
                        JSONObject location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        String lat = location.getString("lat");
                        String lng = location.getString("lng");
                        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        mapContainer.setVisibility(View.VISIBLE);
                        setZoomLevel(10);
                    } else {
                        setPos(state);
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
    protected void setPos(String state) {
        Log.d("setPos", "1");
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + state);
//                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + state + "&dirflg=d");
                Log.d("directions 1", gmmIntentUri.toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        url += (state);
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        Log.d("MAP URL", "addressToCoordinates: " + url);
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")){
                        JSONObject location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        String lat = location.getString("lat");
                        String lng = location.getString("lng");
                        position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        mapContainer.setVisibility(View.VISIBLE);
                        setZoomLevel(25);
                    } else {
                        showMap = false;
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
    protected void setZoomLevel( int distanceInMiles) {
        // 1 mile is approximately 1609.34 meters
        double meters = distanceInMiles * 1609.34;
        // zoom level formula provided by Google Maps API
        float level = (float) (16 - Math.log(meters / 500) / Math.log(2));

        if (position != null){
            Log.d("POS", "setZoomLevel: " + position.toString());
            currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, level));
        }
    }
    protected void initUI(){
        backButton = findViewById(R.id.backButton);
        chatButton = findViewById(R.id.chatButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        trashButton = findViewById(R.id.trashButton);

        imageView = findViewById(R.id.image);
        nameView = findViewById(R.id.name);
        descriptionView = findViewById(R.id.description);
        priceView = findViewById(R.id.price);
        priceContainer = findViewById(R.id.priceContainer);
        bandsContainer = findViewById(R.id.bandsContainer);
        genreView = findViewById(R.id.genre);
        genreContainer = findViewById(R.id.genreContainer);
        agesView = findViewById(R.id.ages);
        agesContainer = findViewById(R.id.agesContainer);
        timeView = findViewById(R.id.time);
        timeContainer = findViewById(R.id.timeContainer);
        capacityView = findViewById(R.id.capacity);
        capacityContainer = findViewById(R.id.capacityContainer);

        addressView = findViewById(R.id.address);
        mapContainer = findViewById(R.id.mapContainer);
        map = findViewById(R.id.map);
        directionsButton = findViewById(R.id.directions);

        spotifyContainer = findViewById(R.id.spotifyContainer);

        feedback = findViewById(R.id.rateTheEvent);
        review = findViewById(R.id.reviews);
        addToCart = findViewById(R.id.addToCart);
        buttonContainer = findViewById(R.id.buttonContainer);

        if (!UserInfo.hasInstance()){
            buttonContainer.setVisibility(View.GONE);
        }
    }
    protected void setUIListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPreview();
            }
        });

        // Set onClickListener for the 'feedback' button
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iFeedback = new Intent(AbstractElementActivity.this, ConcertRating.class);
                startActivity(iFeedback);
            }
        });

        // Set onClickListener for the 'reviews' button
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Reviews Button", "Clicked");
                Intent intent = new Intent(AbstractElementActivity.this, ReviewActivity.class);
                Log.i("Element ID Sending to Reviews", String.valueOf(concert_id));  // Ensure this is getting logged
                intent.putExtra("WSsessionID", concert_id);
                startActivity(intent);
            }
        });

        // Set onClickListener for the 'payment' button
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AbstractElementActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                int id = UserInfo.getInstance().loggedInUser.id;
                Log.i("ID", String.valueOf(id));
                String singlePrice = priceView.getText().toString();
                final String url = "http://coms-309-017.class.las.iastate.edu:8080/carts/"+id;
                Log.i("URL",url);
                String concertName = nameView.getText().toString();
                Map<String, String> params = new HashMap<>();
                if(singlePrice.equals("FREE")) {
                    params.put("pricePerTicket","FREE");
                } else {
                    String[] splitPrice = singlePrice.split("\\$");
                    Log.i("Single Ticket Price",splitPrice[1]);
                    params.put("pricePerTicket",splitPrice[1]);
                }


                Log.i("Name",concertName);
                Log.i("num","1");
                params.put("concertName",concertName);
                params.put("numberOfTickets","1");


                JSONObject jsonObject = new JSONObject(params);
                JsonObjectRequest jorAdd = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i("SUCCESS",jsonObject.toString());
                        Intent iAddToCart = new Intent(AbstractElementActivity.this, CartActivity.class);
                        startActivity(iAddToCart);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Volley Response Error","Error detected while PUTTING "+volleyError.toString());
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jorAdd);
            }
        });

    }
    protected void createElement(int id) {
        String requestURL = childURL + "/" + id;

        Log.d("Abstract Element", "createElement: " + requestURL);

        if (id == -1){
            setInvisible();
            return;
        }

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", "Element response: " + response.toString());
                currentObj = response;
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
    protected void setDetails(JSONObject response){
        //image
        if (response.has("image")){
            try {
                if (!response.getString("image").equals("null")) {
                    String image = response.getString("image");
                    setImageURL(image, imageView);
                    imageView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Log.d("No image link", "Set Details");
            }
        }

        //name
        setOneDetail(response, "name", nameView);

        //description
        setOneDetail(response, "description", descriptionView);

        //genre
        setOneDetail(response, "genre", genreView, genreContainer);

        //ages
        setOneDetail(response, "ages", agesView, agesContainer);
    }
    protected void setOneDetail(JSONObject obj, String id, TextView view, LinearLayout layout){
        try {
            if (obj.has(id)) {
                if (!obj.getString(id).equals("null")) {
                    view.setText(obj.getString(id));
                    layout.setVisibility(View.VISIBLE);
                    return;
                }
            }
            view.setVisibility(View.GONE);
        } catch (Exception e){
            Log.d("NO ELEMENT", "setOneDetail: " + id);
        }
    }
    protected void setOneDetail(JSONObject obj, String id, TextView view){
        try {
            if (obj.has(id)) {
                if (!obj.getString(id).equals("null")) {
                    view.setText(obj.getString(id));
                    view.setVisibility(View.VISIBLE);
                    return;
                }
            }
            view.setVisibility(View.GONE);
        } catch (Exception e){
            Log.d("NO ELEMENT", "setOneDetail: " + id);
        }
    }
    protected void setImageURL(String url, ImageView imageView){
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }
    protected void setInvisible(){
        favoriteButton.setVisibility(View.GONE);
        trashButton.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        descriptionView.setVisibility(View.GONE);
        priceContainer.setVisibility(View.GONE);
        bandsContainer.setVisibility(View.GONE);
        genreContainer.setVisibility(View.GONE);
        agesContainer.setVisibility(View.GONE);
        timeContainer.setVisibility(View.GONE);
        capacityContainer.setVisibility(View.GONE);
        mapContainer.setVisibility(View.GONE);
        chatButton.setVisibility(View.GONE);
        spotifyContainer.setVisibility(View.GONE);
    }

    private void addPreview(){
        if (UserInfo.hasInstance()){
            try {
                int id = intent.getIntExtra("ID", 0);

                String URL = null;
                if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equalsIgnoreCase("band")){
                    URL = Helper.baseURL + "/venues/" + id + "/bands/" + UserInfo.getInstance().loggedInUser.id;
                } else if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equalsIgnoreCase("venue")){
                    URL = Helper.baseURL + "/venues/" + UserInfo.getInstance().loggedInUser.id + "/bands/" + id;
                }

                JsonObjectRequest jsonObjPut = new JsonObjectRequest(Request.Method.PUT, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(AbstractElementActivity.this, ChatHomeActivity.class);
                        intent.putExtra("dest", id);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserProfile", "Error adding chat preview", error);
                    }
                });
                Log.d("put", jsonObjPut.toString());
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjPut);
            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
