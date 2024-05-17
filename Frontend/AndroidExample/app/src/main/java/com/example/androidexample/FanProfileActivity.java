package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Allows a fan to see their profile.
 */
public class FanProfileActivity extends AbstractProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasTitle = true;

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListeners(){
        super.setListeners();
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FanProfileActivity.this, FanEditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
