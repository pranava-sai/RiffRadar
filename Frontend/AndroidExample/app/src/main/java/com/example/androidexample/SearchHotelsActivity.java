package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchHotelsActivity extends AppCompatActivity {
    private EditText city;
    private Button search;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotels);
        city = findViewById(R.id.searchHotelByCity);
        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityToSearch = city.getText().toString();
                Intent intent = new Intent(SearchHotelsActivity.this, HotelList.class);
                intent.putExtra("City Name",cityToSearch);
                startActivity(intent);
            }
        });
    }
}
