package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

/**
 * VenueActivity class represents an activity for managing venue-related information.
 * It extends AbstractElementActivity.
 */
public class VenueActivity extends AbstractElementActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        childURL = Helper.baseURL + "/venues";
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatButton.setVisibility(View.VISIBLE);
    }


    @Override
    protected void setDetails(JSONObject response) {
        super.setDetails(response);

        setOneDetail(response, "capacity", capacityView, capacityContainer);

        try {
            if (response.has("address")) {
                JSONObject address = response.getJSONObject("address");
                String state = address.getString("state");
                if (!state.equals("null")) {
                    String city = address.getString("city");
                    if (!city.equals("null")) {
                        String streetAddress = address.getString("streetAddress");
                        if (!streetAddress.equals("null")) {
                            map.onCreate(null);
                            map.getMapAsync(this);
                            addressView.setText(streetAddress + "\n" + city + ", " + state);
                            setPos(streetAddress, city, state);

                            return;
                        }
                        map.onCreate(null);
                        map.getMapAsync(this);
                        addressView.setText(city + ", " + state);
                        setPos(city, state);

                        return;
                    }
                    map.onCreate(null);
                    map.getMapAsync(this);
                    addressView.setText(state);
                    setPos(state);
                }
            }
        } catch (Exception e) {
            Log.d("Error", "Set Details");
        }
    }
}

