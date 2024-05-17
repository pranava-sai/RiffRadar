package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Allows a venue to see their profile.
 */
public class VenueProfileActivity extends AbstractProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasImage = true;
        hasTitle = true;
        hasDescription = true;
        hasGenre = true;
        hasStreetAddress = true;
        hasCity = true;
        hasState = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListeners(){
        super.setListeners();
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VenueProfileActivity.this, VenueEditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
