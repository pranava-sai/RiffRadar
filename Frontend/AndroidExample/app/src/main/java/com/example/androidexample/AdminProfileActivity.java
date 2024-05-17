package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Allows a fan to see their profile.
 */
public class AdminProfileActivity extends AbstractProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasTitle = true;

        super.onCreate(savedInstanceState);
    }
}
