package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows a guest to see concerts w/o logging in.
 */
public class GuestFeedActivity extends AbstractFeedActivity {

    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set destination
        destination = ConcertActivity.class;

        //decide attributes for class
        priceAttribute = true;
        favoriteAttribute = false;
        chatAttribute = false;
        createAttribute = false;
        groupsAttribute = false;

        //hold url to access concerts
        childURL = Helper.baseURL + "/concerts";

    }

    //helper methods

    @Override
    protected void setUIListeners(){
        super.setUIListeners();
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestFeedActivity.this, GreeterActivity.class);
                UserInfo.resetInstance();
                LocationInfo.resetInstance();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void callDiscovery(){
        //get concerts from discovery api
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?size=10&apikey=GzNoBZf0yFL3gjGfz5mfUs17FC8Abo6o&keyword=music&city=ames";
        if (LocationInfo.getInstance().position.latLng != null){
            Log.d("ticketmaster", "has position" + LocationInfo.getInstance().position.toString());
            url = "https://app.ticketmaster.com/discovery/v2/events.json?size=10&apikey=GzNoBZf0yFL3gjGfz5mfUs17FC8Abo6o&keyword=music&latlong=" + LocationInfo.getInstance().position.latLng.latitude + "," + LocationInfo.getInstance().position.latLng.longitude + "&radius=" + LocationInfo.getInstance().position.radius;
            Log.d("ticketmaster", url);
        }

        getDiscoveryCards(url);
    }
}