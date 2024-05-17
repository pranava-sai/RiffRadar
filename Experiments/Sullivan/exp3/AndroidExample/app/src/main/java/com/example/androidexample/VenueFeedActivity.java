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

public class VenueFeedActivity extends AbstractFeedActivity {

    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //decide attributes for class
        priceAttribute = true;
        favoriteAttribute = false;
        chatAttribute = true;
        createAttribute = true;

        //hold url to access bands
        arrayURL += "/bands";
    }

    //helper methods
    @Override
    void setOneCardListener(View card, int index){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when card is pressed, use intent to switch to Card Activity */
                Intent intent = new Intent(VenueFeedActivity.this, BandActivity.class);
                intent.putExtra("PAGE", String.valueOf(page));
                Element element = elements.get(index);
                setExtras(intent, element);
                startActivity(intent);
            }
        });
    }

    @Override
    void initUI() {
    }
    @Override
    void setExtras(Intent intent, Element element) {
        intent.putExtra("NAME", element.name);
        intent.putExtra("IMAGE", element.image);
        intent.putExtra("LOCATION", element.location);
        intent.putExtra("GENRE", element.genre);
        intent.putExtra("DESCRIPTION", element.description);

    }
    @Override
    void addAttributes(JSONObject jsonObject, Element element) {
        try {
            if (jsonObject.has("location")){
                String location = jsonObject.getString("location");
                element.addLocation(location);
            }

            if (jsonObject.has("image")){
                String image = jsonObject.getString("image");
                element.addImage(image);
            }

            if (jsonObject.has("genre")){
                String genre = jsonObject.getString("genre");
                element.addGenre(genre);
            }

            if (jsonObject.has("description")){
                String description = jsonObject.getString("description");
                element.addDescription(description);
            }

        }  catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    void initBanner(){
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when banner is pressed, use intent to switch to Main Activity */
                Intent intent = new Intent(VenueFeedActivity.this, VenueActivity.class);
                startActivity(intent);
            }
        });
    }
}