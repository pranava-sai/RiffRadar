package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConcertActivity extends AbstractElementActivity {

    @Override
    void abstractInitUI() {
    }

    @Override
    void abstractSetListeners() {

    }

    @Override
    void abstractSetDetails() {
        //bands
        setOneDetail("BANDS", bandsView, bandsContainer);

        //time
        setOneDetail("TIME", timeView, timeContainer);

        //price
        setOneDetail("PRICE", priceView, priceContainer);
    }
}