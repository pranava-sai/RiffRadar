package com.example.androidexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A template for all the feed activities.
 * Contains the shared functionality of the feeds.
 */

public abstract class AbstractFeedActivity extends AppCompatActivity {
    //destination for card listener
    Class<? extends AbstractElementActivity> destination;

    // attributes for class
    protected boolean priceAttribute;
    protected boolean favoriteAttribute;
    protected boolean chatAttribute;
    protected boolean createAttribute;
    protected boolean groupsAttribute;

    // UI elements
    protected ImageView profileButton;
    private ImageView banner;
    private ImageView nextButton;
    private ImageView prevButton;
    private TextView pageNumberDisplay;
    private ImageView menuButton;
    private LinearLayout dropdownMenu;

    //menu icons
    protected ImageView favoriteIcon, chatIcon, createIcon, manageIcon, mapIcon, groupsIcon;

    // cards
    private RecyclerView cardDisplay;
    protected CardAdapter cardAdapter;

    //track what page
    protected int page;
    protected int numberOfCards;

    // get position
    private FusedLocationProviderClient locationClient;
    private static final int PERMISSION_ID = 44;
    private String state;

    // hold correct url
    protected String childURL;


    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        /* initialize UI elements */
        setContentView(R.layout.activity_feed);             // link to feed activity XML
        abstractInitUI();
    }


    @Override
    protected void onStart() {
        super.onStart();

        //hide dropdown
        dropdownMenu.setVisibility(View.GONE);

        /* set screen buttons */
        setUIListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get location
        getUserLocation();

        //refresh content, set to first page
        getCards();
    }

    //helper methods
    protected void abstractInitUI() {
        //UI elements
        profileButton = findViewById(R.id.profileButton);
        banner = findViewById(R.id.banner);
        nextButton = findViewById(R.id.nextPage);
        prevButton = findViewById(R.id.prevPage);
        pageNumberDisplay = findViewById(R.id.pageNumberDisplay);
        menuButton = findViewById(R.id.menuIcon);
        dropdownMenu = findViewById(R.id.dropdownMenu);

        //menu
        favoriteIcon = findViewById(R.id.favoriteIcon);
        chatIcon = findViewById(R.id.chatIcon);
        createIcon = findViewById(R.id.createIcon);
        manageIcon = findViewById(R.id.manageIcon);
        mapIcon = findViewById(R.id.mapIcon);
        groupsIcon = findViewById(R.id.groupsIcon);

        //cards
        cardDisplay = findViewById(R.id.cards);
        cardAdapter = new CardAdapter(getLayoutInflater(), this);
        cardDisplay.setAdapter(cardAdapter);
        cardDisplay.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void setUIListeners() {
        /* click listener for buttons */
        //menu buttons
        if (favoriteAttribute) {
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            favoriteIcon.setVisibility(View.GONE);
        }

        if (chatAttribute) {
            chatIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, ChatHomeActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            chatIcon.setVisibility(View.GONE);
        }

        if (createAttribute) {
            createIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, CreateConcertActivity.class);
                    startActivity(intent);
                }
            });

            manageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, ManageConcertsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            manageIcon.setVisibility(View.GONE);
            createIcon.setVisibility(View.GONE);
        }

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbstractFeedActivity.this, RadiusActivity.class);
                startActivity(intent);
            }
        });

        if (groupsAttribute) {
            groupsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, GroupsHomeActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            groupsIcon.setVisibility(View.GONE);
        }

        /* click listener for buttons */
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* toggle drop down menu */
                if (dropdownMenu.getVisibility() == View.VISIBLE) {
                    dropdownMenu.setVisibility(View.GONE);
                } else if (dropdownMenu.getVisibility() == View.GONE) {
                    dropdownMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        // banner
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 0;
                cardDisplay.scrollToPosition(0);
                getCards();
            }
        });

        //previous button
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page--;
                cardDisplay.scrollToPosition(0);
                getCards();
            }
        });

        //next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                getCards();
                cardDisplay.scrollToPosition(0);
            }
        });

        //set card listeners
        cardAdapter.setOnClickListener(new CardAdapter.OnClickListener() {
            @Override
            public void onClick(int id, String url) {
                Log.d("Listener", "Card Clicked");
                if (id == -999) {
                    Log.d("url", url);
                    try {
                        //attempt to parse and start link
                        Uri uri = Uri.parse(url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(AbstractFeedActivity.this, "Error Launching Site", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    return;
                }

                if (UserInfo.hasInstance()) {
                    Class<? extends AbstractElementActivity> destination;
                    String type = UserInfo.getInstance().loggedInUser.loginInfo.userType;

                    if (type.equals("attendee")) {
                        destination = ConcertActivity.class;
                    } else if (type.equals("band")) {
                        destination = VenueActivity.class;
                    } else if (type.equals("venue")) {
                        destination = BandActivity.class;
                    } else {
                        return;
                    }
                    Intent intent = new Intent(AbstractFeedActivity.this, destination);
                    intent.putExtra("ConcertID", id);
                    Log.i("ID Sending", String.valueOf(id));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AbstractFeedActivity.this, ConcertActivity.class);
                    intent.putExtra("ID", id);
                    Log.i("ID Sending", String.valueOf(id));
                    startActivity(intent);
                }
            }
        });

    }

    protected void setButtons() {
        pageNumberDisplay.setText(String.valueOf(page + 1));

        if (numberOfCards < (page + 1) * 10) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }

        if (page <= 0) {
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }
    }

    protected void setImageURL(String url, ImageView imageView) {
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }

    protected void getCards() {
        numberOfCards = 0;

        //empty recycler
        cardAdapter.clearCards();

        String url = childURL;

        if (state != null) {
            url = childURL + "/addresses/" + state;
        }

        Log.d("getCards", "URL: " + url);

        //get stored items
        JsonArrayRequest getCards = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Volley Response", "response: " + response.toString());

                try {
                    int length = response.length();
                    numberOfCards += length;

                    int i = 0;
                    for (i = page * 10; (i < (page + 1) * 10) && (i < length); i++) {
                        cardAdapter.addCard(response.getJSONObject(i));
                    }
                    if (i >= length) {
                        callDiscovery();
                    } else {
                        setButtons();
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
        Log.d("GET", "get cards: " + getCards.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(getCards);
    }

    protected void callDiscovery() {
        setButtons();
    }

    protected void getDiscoveryCards(String url) {
        JsonObjectRequest getDiscovery = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", "response: " + response.toString());
                try {
                    JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
                    int length = events.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject concert = new JSONObject();
                        JSONObject event = events.getJSONObject(i);
                        //name
                        concert.put("name", event.getString("name"));
                        //price
                        try {
                            int min = event.getJSONArray("priceRanges").getJSONObject(0).getInt("min");
                            int max = event.getJSONArray("priceRanges").getJSONObject(0).getInt("max");
                            String price = "$" + min + " to $" + max;
                            if (min == max) {
                                if (min == 0) {
                                    price = "Free";
                                } else {
                                    price = "$" + min;
                                }
                            }
                            concert.put("price", price);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //set id
                        try {
                            concert.put("id", -999); // unique id (never created by the server) to show it is api generated
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //url
                        try {
                            concert.put("url", event.getString("url"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //image
                        try {
                            concert.put("image", event.getJSONArray("images").getJSONObject(0).getString("url"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        cardAdapter.addCard(concert);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setButtons();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "error: " + error.toString());
                        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=10&apikey=GzNoBZf0yFL3gjGfz5mfUs17FC8Abo6o&keyword=music&city=ames";
                        getDiscoveryCards(url);
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(getDiscovery);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                locationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d("Get location", "response");
                        Location location = task.getResult();
                        if (location != null) {
                            Log.d("Get location", "not null");
                            LocationInfo.getInstance().position.latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            coordinatesToAddress(LocationInfo.getInstance().position.latLng);
                            Log.d("SET LOCATION", "lat: " + location.getLatitude() + "lng: " + location.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest on FusedLocationClient
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();

            LocationInfo.getInstance().position.latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Method to be used with Location Services.
     * Is called when the user enables or disables permissions from the pop up.
     * When granted, gets their location.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void getUserLocation() {
        prevButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        if (LocationInfo.getInstance().position.radius == 0) {
            LocationInfo.getInstance().position.radius = 15;
        }

        if (LocationInfo.getInstance().position.latLng == null || (LocationInfo.getInstance().position.latLng.latitude == 0 && LocationInfo.getInstance().position.latLng.longitude == 0)) {
            getLocation();
        }

        coordinatesToAddress(LocationInfo.getInstance().position.latLng);
    }

    private void coordinatesToAddress(LatLng latLng) {
        if (latLng == null){
            return;
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        url += latLng.latitude + "," + latLng.longitude;
        url += "&key=AIzaSyD7v84bgnyK78C8u7JgpkO1qmGDGe6XPK4";
        JsonObjectRequest jsonArrReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("coordinatesToAddress", response.toString());
                // Parse the JSON array and add data to the adapter
                try {
                    if (response.getString("status").equals("OK")) {
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject addressComponents = results.getJSONObject(0);
                            JSONArray addressArray = addressComponents.getJSONArray("address_components");
                            for (int i = 0; i < addressArray.length(); i++) {
                                JSONObject address = addressArray.getJSONObject(i);
                                JSONArray types = address.getJSONArray("types");
                                if (types != null && types.length() > 0) {
                                    for (int j = 0; j < types.length(); j++) {
                                        if (types.getString(j).equals("administrative_area_level_1")) {
                                            state = address.getString("long_name");
                                            Log.d("Reverse Geocode", state);
                                            getCards();
                                        }
                                    }
                                }
                            }
                        }
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
