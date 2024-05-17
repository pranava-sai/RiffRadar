package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A template for the profile pages.
 * Includes shared behavior like getting information, sending information, and UI connections and behavior.
 */
public abstract class AbstractProfileActivity extends AppCompatActivity {
    private ImageView backButton, menuIcon;
    private LinearLayout dropdownMenu;

    protected View passwordButton, emailButton, editProfileButton, logoutButton, deleteButton;
    protected LinearLayout imageWrapper, titleWrapper, genreWrapper, streetWrapper, cityWrapper, stateWrapper, descriptionWrapper;
    protected TextView title, genre, streetAddress, city, state, description;
    private ImageView imagePreview;

    protected boolean hasImage, hasTitle, hasGenre, hasDescription, hasStreetAddress, hasCity, hasState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* initialize UI elements */
        initElements();
    }

    @Override
    protected void onResume() {
        super.onResume();

        autofill();

        /* default dropdown to hidden */
        dropdownMenu.setVisibility(View.GONE);

        /* hide unused fields */
        showFields();

        /* set listeners */
        setListeners();
    }

    protected void autofill() {
        String url = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase() + "s/" + UserInfo.getInstance().loggedInUser.id;
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", "response: " + response.toString());
                try {
                    Log.d("profile autofill", "updated: " + response.toString());

                    if (response.has("image")){
                        LoadImage loader = new LoadImage(imagePreview);
                        loader.execute(response.getString("image"));
                    }

                    if (response.has("name")){
                        if (!response.getString("name").equals("null")){
                            title.setText(response.getString("name"));
                        }
                    }

                    if (response.has("genre")){
                        if (!response.getString("genre").equals("null")){
                            genre.setText(response.getString("genre"));
                        }
                    }

                    if (response.has("description")){
                        if (!response.getString("description").equals("null")){
                            description.setText(response.getString("description"));
                        }
                    }

                    try {
                        if (response.has("address")){
                            Object addressObject = response.get("address");
                            Log.d("profile autofill()", "address" + addressObject.toString());
                            if (addressObject instanceof JSONObject){
                                JSONObject address = response.getJSONObject("address");

                                String streetAddressText = address.getString("streetAddress");
                                if (!streetAddressText.equals("null")){
                                    streetAddress.setText(streetAddressText);
                                }

                                String cityText = address.getString("city");
                                if (!cityText.equals("null")){
                                    city.setText(cityText);
                                }

                                String stateText = address.getString("state");
                                if (!stateText.equals("null")){
                                    state.setText(stateText);
                                }
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "error: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        Log.d("GET", "autofill: " + objRequest.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objRequest);
    }
    protected void showFields(){
        if (hasImage){
            imageWrapper.setVisibility(View.VISIBLE);
        } else {
            imageWrapper.setVisibility(View.GONE);
        }
        if (hasTitle){
            titleWrapper.setVisibility(View.VISIBLE);
        } else {
            titleWrapper.setVisibility(View.GONE);
        }

        if (hasGenre){
            genreWrapper.setVisibility(View.VISIBLE);
        } else {
            genreWrapper.setVisibility(View.GONE);
        }

        if (hasStreetAddress){
            streetWrapper.setVisibility(View.VISIBLE);
        } else {
            streetWrapper.setVisibility(View.GONE);
        }

        if (hasCity){
            cityWrapper.setVisibility(View.VISIBLE);
        } else {
            cityWrapper.setVisibility(View.GONE);
        }

        if (hasState){
            stateWrapper.setVisibility(View.VISIBLE);
        } else {
            stateWrapper.setVisibility(View.GONE);
        }

        if (hasDescription){
            descriptionWrapper.setVisibility(View.VISIBLE);
        } else {
            descriptionWrapper.setVisibility(View.GONE);
        }
    }

    protected void initElements(){
        backButton = findViewById(R.id.backButton);
        menuIcon = findViewById(R.id.menuIcon);
        dropdownMenu = findViewById(R.id.dropdownMenu);

        passwordButton = findViewById(R.id.passwordButton);
        emailButton = findViewById(R.id.emailButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        deleteButton = findViewById(R.id.deleteAccountButton);

        imageWrapper = findViewById(R.id.imageWrapper);
        titleWrapper = findViewById(R.id.titleWrapper);
        genreWrapper = findViewById(R.id.genreWrapper);
        streetWrapper = findViewById(R.id.streetWrapper);
        cityWrapper = findViewById(R.id.cityWrapper);
        stateWrapper = findViewById(R.id.stateWrapper);
        descriptionWrapper = findViewById(R.id.descriptionWrapper);

        imagePreview = findViewById(R.id.imagePreview);

        title = findViewById(R.id.title);
        genre = findViewById(R.id.genre);
        streetAddress = findViewById(R.id.streetAddress);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        description = findViewById(R.id.description);

    }
    protected void setListeners(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownMenu.getVisibility() == View.VISIBLE){
                    dropdownMenu.setVisibility(View.GONE);
                } else if (dropdownMenu.getVisibility() == View.GONE){
                    dropdownMenu.setVisibility(View.VISIBLE);
                }
            }
        });


        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "PW Button");
                Intent intent = new Intent(AbstractProfileActivity.this, EmailUserVerify.class);
                startActivity(intent);
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Email Button");
                Intent intent = new Intent(AbstractProfileActivity.this, ChangeEmail.class);
                startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase() + "s/" + UserInfo.getInstance().loggedInUser.id;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(AbstractProfileActivity.this, GreeterActivity.class);
                        Toast.makeText(AbstractProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserProfile", "Error deleting user", error);
                        // You might want to inform the user about the error
                        Toast.makeText(AbstractProfileActivity.this, "Error deleting user", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.resetInstance();
                LocationInfo.resetInstance();
                Intent intent = new Intent(AbstractProfileActivity.this, GreeterActivity.class);
                Toast.makeText(AbstractProfileActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }

    protected void setImageURL(String url, ImageView imageView){
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }
}