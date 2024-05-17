package com.example.androidexample;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractElementActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton backButton;  // define username textview variable
    ImageView banner;
    TextView nameView, descriptionView, genreView, agesView, addressView, priceView, bandsView, timeView;
    ImageView imageView;
    LinearLayout agesContainer, genreContainer, mapContainer, priceContainer, bandsContainer, timeContainer;
    MapView map;
    LatLng position;
    Intent intent;
    GoogleMap currentMap;

    private static final String MAPVIEW_BUNDLE_KEY = "MAPVIEW_BUNDLE_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);             // link to Band activity XML

        /* initialize UI elements */
        initUI();

        /* default to invisible */
        setInvisible();

        /* get intent */
        intent = getIntent();

        //map
        position = new LatLng(0, 0);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        map = (MapView) findViewById(R.id.map);
        map.onCreate(mapViewBundle);

        map.getMapAsync(this);

        setUIListeners();
        setDetails();
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


    void setZoomLevel(int distanceInMiles) {
        // 1 mile is approximately 1609.34 meters
        double meters = distanceInMiles * 1609.34;
        // zoom level formula provided by Google Maps API
        float level = (float) (16 - Math.log(meters / 500) / Math.log(2));
        currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, level));

    }
    void initUI(){
        backButton = findViewById(R.id.backButton);      // link to greeter
        banner = findViewById(R.id.banner);

        imageView = findViewById(R.id.image);
        nameView = findViewById(R.id.name);
        descriptionView = findViewById(R.id.description);
        priceView = findViewById(R.id.price);
        priceContainer = findViewById(R.id.priceContainer);
        bandsView = findViewById(R.id.bands);
        bandsContainer = findViewById(R.id.bandsContainer);
        genreView = findViewById(R.id.genre);
        genreContainer = findViewById(R.id.genreContainer);
        agesView = findViewById(R.id.ages);
        agesContainer = findViewById(R.id.agesContainer);
        timeView = findViewById(R.id.time);
        timeContainer = findViewById(R.id.timeContainer);
        addressView = findViewById(R.id.address);
        mapContainer = findViewById(R.id.mapContainer);
        map = findViewById(R.id.map);

        abstractInitUI();
    }
    void setUIListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when login button is pressed, use intent to switch to Greeter Activity */
                Intent intent = new Intent(AbstractElementActivity.this, VenueFeedActivity.class);
                startActivity(intent);
            }
        });

        abstractSetListeners();
    }
    void setDetails(){
        //image
        String image = intent.getStringExtra("IMAGE");
        if (image != null){
            setImageURL(image, imageView);
            scaleImage(imageView);
            imageView.setVisibility(View.VISIBLE);
        }

        //name
        setOneDetail("NAME", nameView);

        //description
        setOneDetail("DESCRIPTION", descriptionView);

        //genre
        setOneDetail("GENRE", genreView, genreContainer);

        //ages
        setOneDetail("AGES", agesView, agesContainer);

        //directions
        String address = intent.getStringExtra("LOCATION");
        if (address != null){
            addressView.setText(address);
            mapContainer.setVisibility(View.VISIBLE);
        }

        abstractSetDetails();
    }
    void setOneDetail(String id, TextView view, LinearLayout layout){
        String extra = intent.getStringExtra(id);
        if (extra != null){
            view.setText(extra);
            layout.setVisibility(View.VISIBLE);
        }
    }
    void setOneDetail(String id, TextView view){
        String extra = intent.getStringExtra(id);
        if (extra != null){
            view.setText(extra);
            view.setVisibility(View.VISIBLE);
        }
    }
    void setImageURL(String url, ImageView imageView){
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }
    void scaleImage(ImageView cardImage) {
        int layoutWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int imgWidth = cardImage.getDrawable().getIntrinsicWidth();
        int imgHeight = cardImage.getDrawable().getIntrinsicHeight();
        float ratio = imgHeight / ((float) imgWidth);
        cardImage.getLayoutParams().height = ((int) (layoutWidth * ratio));
        cardImage.requestLayout();
    }
    void setInvisible(){
        imageView.setVisibility(View.GONE);
        descriptionView.setVisibility(View.GONE);
        priceContainer.setVisibility(View.GONE);
        bandsContainer.setVisibility(View.GONE);
        genreContainer.setVisibility(View.GONE);
        agesContainer.setVisibility(View.GONE);
        timeContainer.setVisibility(View.GONE);
        mapContainer.setVisibility(View.GONE);
    }


    abstract void abstractInitUI();
    abstract void abstractSetListeners();
    abstract void abstractSetDetails();
}