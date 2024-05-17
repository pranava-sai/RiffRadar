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
 * Allows a venue to see bands.
 */
public class VenueFeedActivity extends AbstractFeedActivity {

    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set destination
        destination = BandActivity.class;

        //decide attributes for class
        priceAttribute = true;
        favoriteAttribute = false;
        chatAttribute = true;
        createAttribute = true;
        groupsAttribute = false;

        //hold url to access bands
        childURL = Helper.baseURL + "/bands";
    }

    //helper methods
    @Override
    protected void setUIListeners(){
        super.setUIListeners();
        //button to go to profile page
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VenueFeedActivity.this, VenueProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}