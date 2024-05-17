package com.example.androidexample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MyBookings extends AppCompatActivity {

    private ImageView banner;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);

        banner = findViewById(R.id.banner);

//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            banner.setImageResource(R.drawable.rifflight);
//        } else {
//            banner.setImageResource(R.drawable.riff);
//        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                banner.setImageResource(R.drawable.rifflight);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                banner.setImageResource(R.drawable.riff);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            banner.setImageResource(R.drawable.rifflight);
//        } else {
//            banner.setImageResource(R.drawable.riff);
//        }

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                banner.setImageResource(R.drawable.rifflight);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                banner.setImageResource(R.drawable.riff);
                break;
        }
    }
}
