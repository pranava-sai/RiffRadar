package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Allows a fan to view concerts.
 */
public class FanFeedActivity extends AbstractFeedActivity {
    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set destination
        destination = ConcertActivity.class;

        //decide attributes for class
        priceAttribute = true;
        favoriteAttribute = true;
        chatAttribute = false;
        createAttribute = false;
        groupsAttribute = true;

        //hold url to access concerts
        childURL = Helper.baseURL + "/concerts";
    }

    //helper methods
    @Override
    protected void setUIListeners(){
        super.setUIListeners();
        //button to go to profile page
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FanFeedActivity.this, FanProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void callDiscovery(){
        //get concerts from discovery api
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=10&apikey=GzNoBZf0yFL3gjGfz5mfUs17FC8Abo6o&keyword=music&latlong=" + LocationInfo.getInstance().position.latLng.latitude + "," + LocationInfo.getInstance().position.latLng.longitude + "&radius=" + LocationInfo.getInstance().position.radius;
        getDiscoveryCards(url);
    }
}