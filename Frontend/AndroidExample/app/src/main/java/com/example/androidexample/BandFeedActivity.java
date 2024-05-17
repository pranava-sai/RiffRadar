package com.example.androidexample;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Shows venues to a band user.
 */
public class BandFeedActivity extends AbstractFeedActivity {

    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set destination
        destination = VenueActivity.class;

        //decide attributes for class
        priceAttribute = false;
        favoriteAttribute = false;
        chatAttribute = true;
        createAttribute = false;
        groupsAttribute = false;

        //hold url to access venues
        childURL = Helper.baseURL + "/venues";
    }

    //helper methods
    @Override
    protected void setUIListeners(){
        super.setUIListeners();
        //button to go to profile page
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BandFeedActivity.this, BandProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}