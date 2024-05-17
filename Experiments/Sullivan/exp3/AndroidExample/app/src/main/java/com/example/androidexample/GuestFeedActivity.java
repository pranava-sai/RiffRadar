package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONObject;

public class GuestFeedActivity extends AbstractFeedActivity {
    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //decide attributes for class
        priceAttribute = true;
        favoriteAttribute = false;
        chatAttribute = false;
        createAttribute = false;

        //hold url to access concerts
        arrayURL += "/concerts";
    }


    //helper methods
    @Override
    void setOneCardListener(View card, int index){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when concert is pressed, use intent to switch to Card Activity */
                Intent intent = new Intent(GuestFeedActivity.this, ConcertActivity.class);
                intent.putExtra("PAGE", String.valueOf(page));
                Element element = elements.get(index);
                setExtras(intent, element);
                startActivity(intent);
            }
        });
    }
    @Override
    void initUI() {
        cardPrice0 = findViewById(R.id.card0Price);
        cardPrice1 = findViewById(R.id.card1Price);
        cardPrice2 = findViewById(R.id.card2Price);
        cardPrice3 = findViewById(R.id.card3Price);
        cardPrice4 = findViewById(R.id.card4Price);
        cardPrice5 = findViewById(R.id.card5Price);
        cardPrice6 = findViewById(R.id.card6Price);
        cardPrice7 = findViewById(R.id.card7Price);
        cardPrice8 = findViewById(R.id.card8Price);
        cardPrice9 = findViewById(R.id.card9Price);
    }
    @Override
    void setExtras(Intent intent, Element concert){
        intent.putExtra("NAME", concert.name);
        intent.putExtra("PRICE", concert.price);
        intent.putExtra("IMAGE", concert.image);
        intent.putExtra("LOCATION", concert.location);
        intent.putExtra("TIME", concert.time);
        intent.putExtra("BANDS", concert.bands);
        intent.putExtra("GENRE", concert.genre);
        intent.putExtra("AGES", concert.ages);
        intent.putExtra("DESCRIPTION", concert.description);
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

            if (jsonObject.has("price")){
                String price = jsonObject.getString("price");
                element.addPrice(price);
            }

            if (jsonObject.has("time")){
                String time = jsonObject.getString("time");
                element.addTime(time);
            }

            if (jsonObject.has("bands")){
                String bands = jsonObject.getString("bands");
                element.addBands(bands);
            }

            if (jsonObject.has("genre")){
                String genre = jsonObject.getString("genre");
                element.addGenre(genre);
            }

            if (jsonObject.has("ages")){
                String ages = jsonObject.getString("ages");
                element.addAges(ages);
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
                /* when venue is pressed, use intent to switch to Main Activity */
                Intent intent = new Intent(GuestFeedActivity.this, GuestFeedActivity.class);
                startActivity(intent);
            }
        });
    }
}