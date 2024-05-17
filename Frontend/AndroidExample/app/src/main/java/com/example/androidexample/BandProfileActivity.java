package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Shows a band their profile.
 * Includes paths to change features of their profile.
 */
public class BandProfileActivity extends AbstractProfileActivity {
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

    @Override
    protected void setListeners(){
        super.setListeners();
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BandProfileActivity.this, BandEditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
