package com.example.androidexample;

import android.os.Bundle;

/**
 * Allows a venue to edit their profile.
 */
public class VenueEditProfileActivity extends AbstractEditProfileActivity {
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
}
