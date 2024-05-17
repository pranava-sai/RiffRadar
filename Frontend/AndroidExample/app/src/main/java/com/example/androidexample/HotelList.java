package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;

public class HotelList extends AppCompatActivity {
    private TextView hotelName;
    private TextView hotelAddress;
    private TextView roomRate;
    private RatingBar stars;
    private String city;
    private String cityCode;
    private Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);
        hotelAddress = findViewById(R.id.hotel_address);
//        hotelName = findViewById(R.id.hotel_name);
//        roomRate = findViewById(R.id.hotel_price);
//        stars = findViewById(R.id.hotel_rating);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelList.this, GreeterActivity.class);
                startActivity(intent);
            }
        });

        city = getIntent().getStringExtra("City Name");

        switch (city) {
            case "Birmingham":
                cityCode = "BHM";
                break;
            case "Huntsville":
                cityCode = "HSV";
                break;
            case "Anchorage":
                cityCode = "ANC";
                break;
            case "Fairbanks":
                cityCode = "FAI";
                break;
            case "Phoenix":
                cityCode = "PHX";
                break;
            case "Tucson":
                cityCode = "TUS";
                break;
            case "Little Rock":
                cityCode = "LIT";
                break;
            case "Fayetteville":
                cityCode = "XNA";
                break;
            case "Los Angeles":
                cityCode = "LAX";
                break;
            case "San Francisco":
                cityCode = "SFO";
                break;
            case "Las Vegas":
                cityCode = "LAS";
                break;
            case "Denver":
                cityCode = "DEN";
                break;
            case "Colorado Springs":
                cityCode = "COS";
                break;
            case "Hartford":
                cityCode = "BDL";
                break;
            case "Wilmington":
                cityCode = "ILG";
                break;
            case "Miami":
                cityCode = "MIA";
                break;
            case "Orlando":
                cityCode = "MCO";
                break;
            case "Atlanta":
                cityCode = "ATL";
                break;
            case "Savannah":
                cityCode = "SAV";
                break;
            case "Honolulu":
                cityCode = "HNL";
                break;
            case "Kahului":
                cityCode = "OGG";
                break;
            case "Boise":
                cityCode = "BOI";
                break;
            case "Idaho Falls":
                cityCode = "IDA";
                break;
            case "Chicago":
                cityCode = "ORD";
                break;
            case "Chicago Midway":
                cityCode = "MDW";
                break;
            case "Indianapolis":
                cityCode = "IND";
                break;
            case "South Bend":
                cityCode = "SBN";
                break;
            case "Des Moines":
                cityCode = "DSM";
                break;
            case "Cedar Rapids":
                cityCode = "CID";
                break;
            case "Wichita":
                cityCode = "ICT";
                break;
            case "Kansas City":
                cityCode = "MCI";
                break;
            case "Louisville":
                cityCode = "SDF";
                break;
            case "Lexington":
                cityCode = "LEX";
                break;
            case "New Orleans":
                cityCode = "MSY";
                break;
            case "Baton Rouge":
                cityCode = "BTR";
                break;
            case "Portland":
                cityCode = "PWM";
                break;
            case "Bangor":
                cityCode = "BGR";
                break;
            case "Baltimore":
                cityCode = "BWI";
                break;
            case "Washington":
                cityCode = "DCA";
                break;
            case "Boston":
                cityCode = "BOS";
                break;
            case "Worcester":
                cityCode = "ORH";
                break;
            case "Detroit":
                cityCode = "DTW";
                break;
            case "Grand Rapids":
                cityCode = "GRR";
                break;
            case "Minneapolis":
                cityCode = "MSP";
                break;
            case "Rochester":
                cityCode = "RST";
                break;
            case "Jackson":
                cityCode = "JAN";
                break;
            case "Gulfport":
                cityCode = "GPT";
                break;
            case "St.Louis":
                cityCode = "STL";
                break;
            case "Billings":
                cityCode = "BIL";
                break;
            case "Missoula":
                cityCode = "MSO";
                break;
            case "Omaha":
                cityCode = "OMA";
                break;
            case "Reno":
                cityCode = "RNO";
                break;
            case "Manchester":
                cityCode = "MHT";
                break;
            case "Portsmouth":
                cityCode = "PSM";
                break;
            case "Newark":
                cityCode = "EWR";
                break;
            case "Atlantic City":
                cityCode = "ACY";
                break;
            case "Albuquerque":
                cityCode = "ABQ";
                break;
            case "Santa Fe":
                cityCode = "SAF";
                break;
            case "New York City":
                cityCode = "JFK";
                break;
            case "Charlotte":
                cityCode = "CLT";
                break;
            case "Raliegh":
                cityCode = "RDU";
                break;
            case "Fargo":
                cityCode = "FAR";
                break;
            case "Bismarck":
                cityCode = "BIS";
                break;
            case "Columbus":
                cityCode = "CMH";
                break;
            case "Cleveland":
                cityCode = "CLE";
                break;
            case "Oklahoma City":
                cityCode = "OKC";
                break;
            case "Tulsa":
                cityCode = "TUL";
                break;
            case "Portland Oregon":
                cityCode = "PDX";
                break;
            case "Eugene":
                cityCode = "EUG";
                break;
            case "Philadelphia":
                cityCode = "PHL";
                break;
            case "Pittsburgh":
                cityCode = "PIT";
                break;
            case "Providence":
                cityCode = "PVD";
                break;
            case "Newport":
                cityCode = "NPT";
                break;
            case "Charleston":
                cityCode = "CHS";
                break;
            case "Columbia":
                cityCode = "CAE";
                break;
            case "Sioux Falls":
                cityCode = "FSD";
                break;
            case "Rapid City":
                cityCode = "RAP";
                break;
            case "Nashville":
                cityCode = "BNA";
                break;
            case "Memphis":
                cityCode = "MEM";
                break;
            case "Dallas":
                cityCode = "DFW";
                break;
            case "Houston":
                cityCode = "IAH";
                break;
            case "Salt Lake City":
                cityCode = "SLC";
                break;
            case "Provo":
                cityCode = "PVU";
                break;
            case "Burlington":
                cityCode = "BTV";
                break;
            case "Rutland":
                cityCode = "RUT";
                break;
            case "Norfolk":
                cityCode = "ORF";
                break;
            case "Seattle":
                cityCode = "SEA";
                break;
            case "Spokane":
                cityCode = "GEG";
                break;
            case "Charleston - West Virginia":
                cityCode = "CRW";
                break;
            case "Huntington":
                cityCode = "HTS";
                break;
            case "Milwaukee":
                cityCode = "MKE";
                break;
            case "Madison":
                cityCode = "MSN";
                break;
            case "Cheyenne":
                cityCode = "CYS";
                break;
            case "Jackson Hole":
                cityCode = "JAC";
                break;
            default:
                cityCode = null;
        }
        try {
            HotelListings(cityCode);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private void HotelListings(String iataCode) throws ResponseException{
        Amadeus amadeus = Amadeus
                .builder("MhGWRFjunAmDkMZaplzYtXX1Ahk9oZ0k", "7FtFOpS04TIT9kdg")
                .build();

        Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", iataCode));

        if (hotels[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + hotels[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        System.out.println(hotels[0]);

        String address = String.valueOf(hotels[0]);
        hotelAddress.setText(address);
    }
}