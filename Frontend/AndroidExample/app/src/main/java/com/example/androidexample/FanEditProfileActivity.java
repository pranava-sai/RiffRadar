package com.example.androidexample;

import android.os.Bundle;

/**
 * Allows a fan to edit their profile.
 */
public class FanEditProfileActivity extends AbstractEditProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasTitle = true;

        super.onCreate(savedInstanceState);
    }
}
