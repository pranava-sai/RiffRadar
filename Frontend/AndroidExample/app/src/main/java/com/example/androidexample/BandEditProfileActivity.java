package com.example.androidexample;

import android.os.Bundle;

/**
 * Allows a band to edit their profile.
 */
public class BandEditProfileActivity extends AbstractEditProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasImage = true;
        hasTitle = true;
        hasDescription = true;
        hasGenre = true;
        hasCity = true;
        hasState = true;

        super.onCreate(savedInstanceState);
    }
}
