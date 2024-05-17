package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A template for the edit profile activities.
 * Includes shared behavior like parsing information, declaring and connecting UI, and sending requests.
 */
public abstract class AbstractEditProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private View updateButton;

    private LinearLayout imageWrapper, titleWrapper, genreWrapper, streetWrapper, cityWrapper, stateWrapper, descriptionWrapper;
    private EditText enterTitle, enterDescription, enterStreetAddress, enterCity;
    private ImageView imagePreview;
    private Spinner genreDropdown, stateDropdown;

    protected JSONObject currentProfile = new JSONObject();

    private final String[] STATES = {"State", "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
    private ArrayAdapter<String> stateAdapter;

    private ArrayList<String> GENRES = new ArrayList<String>();

    private ActivityResultLauncher<String> getImage;
    private Uri selectiedUri;

    private final String UPLOAD_URL = "http://10.0.2.2:8080/images";

    protected boolean hasImage, hasTitle, hasGenre, hasDescription, hasStreetAddress, hasCity, hasState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        /* initialize UI elements */
        initUI();

        /* set state spinner */
        stateDropdown = findViewById(R.id.stateDropdown);
        stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, STATES);
        stateAdapter.setDropDownViewResource(R.layout.dropdown_item);
        stateDropdown.setAdapter(stateAdapter);

        /* set genre spinner */
        genreDropdown = findViewById(R.id.genreDropdown);
        //default
        GENRES.add("Genre");
        GENRES.add("Rock");
        GENRES.add("Country");
        GENRES.add("Rap");
        setGenreSpinner();
        //attempt to load from server
        setGenreList(); //calls autofill() when done
        /* only show chosen */
        showFields();

        /* register image picker */
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri != null) {
                        selectiedUri = uri;
                        imagePreview.setImageURI(uri);
                    }
                });

        /* set listeners */
        setListeners();

    }

    protected void initUI() {
        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);

        imageWrapper = findViewById(R.id.imageWrapper);
        titleWrapper = findViewById(R.id.titleWrapper);
        genreWrapper = findViewById(R.id.genreWrapper);
        streetWrapper = findViewById(R.id.streetWrapper);
        cityWrapper = findViewById(R.id.cityWrapper);
        stateWrapper = findViewById(R.id.stateWrapper);
        descriptionWrapper = findViewById(R.id.descriptionWrapper);

        enterTitle = findViewById(R.id.enterTitle);
        imagePreview = findViewById(R.id.imagePreview);
        enterDescription = findViewById(R.id.enterDescription);

        enterStreetAddress = findViewById(R.id.enterStreetAddress);
        enterCity = findViewById(R.id.enterCity);
    }

    protected void setListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagePreview.setOnClickListener(v -> getImage.launch("image/*"));

        enterTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(AbstractEditProfileActivity.this);

                    //exit
                    return true;
                }
                return false;
            }
        });

        enterStreetAddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(AbstractEditProfileActivity.this);

                    //exit
                    return true;
                }
                return false;
            }
        });

        enterCity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(AbstractEditProfileActivity.this);

                    //exit
                    return true;
                }
                return false;
            }
        });

        enterDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(AbstractEditProfileActivity.this);

                    //exit
                    return true;
                }
                return false;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                finish();
            }
        });
    }

    protected void updateProfile() {
        JSONObject profile = currentProfile;
        Log.d("old profile", currentProfile.toString());
        //title
        if (hasTitle) {
            if (enterTitle.getText() != null && !enterTitle.getText().toString().equals("")) {
                updateField("name", enterTitle.getText().toString());
            }
        }

        //genre
        if (hasGenre) {
            if (!genreDropdown.getSelectedItem().toString().equals("Genre")) {
                updateField("genre", genreDropdown.getSelectedItem().toString());
            }
        }

        //streetAddress
        String streetAddressToAdd = null;
        if (hasStreetAddress) {
            if (enterStreetAddress.getText() != null && !enterStreetAddress.getText().toString().equals("")) {
                streetAddressToAdd = enterStreetAddress.getText().toString();
            }
        }

        //city
        String cityToAdd = null;
        if (hasCity) {
            if (enterCity.getText() != null && !enterCity.getText().toString().equals("")) {
                cityToAdd = enterCity.getText().toString();
            }
        }

        //state
        String stateToAdd = null;
        if (hasState) {
            if (!stateDropdown.getSelectedItem().toString().equals("State")) {
                stateToAdd = stateDropdown.getSelectedItem().toString();
            }
        }

        // add new address
        if (hasStreetAddress || hasCity || hasState) {
            try {
                if (profile.has("address")) {
                    Object addressObject = profile.get("address");
                    Log.d("Address Put", "old address : " + addressObject.toString());
                    if (addressObject instanceof JSONObject) {
                        JSONObject oldAddress = profile.getJSONObject("address");
                        if (streetAddressToAdd != null) {
                            oldAddress.put("streetAddress", streetAddressToAdd);
                        }

                        if (cityToAdd != null) {
                            oldAddress.put("city", cityToAdd);
                        }

                        if (stateToAdd != null) {
                            oldAddress.put("state", stateToAdd);
                        }

                        updateAddress(oldAddress);
                    } else {
                        JSONObject newAddress = new JSONObject();

                        newAddress.put("streetAddress", streetAddressToAdd);
                        newAddress.put("city", cityToAdd);
                        newAddress.put("state", stateToAdd);
                        newAddress.put("zipcode", null);

                        updateAddress(newAddress);
                    }
                } else {
                    JSONObject newAddress = new JSONObject();

                    newAddress.put("streetAddress", streetAddressToAdd);
                    newAddress.put("city", cityToAdd);
                    newAddress.put("city", stateToAdd);
                    newAddress.put("zipcode", null);

                    updateAddress(newAddress);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (selectiedUri != null){
            uploadImage(UserInfo.getInstance().loggedInUser.id);
        }


        //description
        updateField("description", enterDescription.getText().toString());
    }

    protected void updateField(String field, String newInfo) {
        String addProfileURL = null;
        if (UserInfo.hasInstance()) {
            addProfileURL = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType + "s/" + UserInfo.getInstance().loggedInUser.id;
        }
        Log.d("Send profile", "URL: " + addProfileURL);
        try {

            JSONObject change = new JSONObject();
            change.put(field, newInfo);

            JsonObjectRequest changeProfile = new JsonObjectRequest(Request.Method.PUT, addProfileURL, change, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject result) {
                    Log.d("Add Profile", "onResponse: " + result.toString());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());
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
            // Adding request to request queue
            Log.d("Edit Profile Put", "request: " + changeProfile.toString());
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(changeProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateAddress(JSONObject newAddress) {
        String changeProfileURL = null;
        if (UserInfo.hasInstance()) {
            changeProfileURL = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType + "s/" + UserInfo.getInstance().loggedInUser.id;
        }
        Log.d("updateAddress()", "URL: " + changeProfileURL);

        try {
            JSONObject change = new JSONObject();
            change.put("address", newAddress);
            Log.d("address put", "change: " + change.toString());

            JsonObjectRequest changeProfile = new JsonObjectRequest(Request.Method.PUT, changeProfileURL, change, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject result) {
                    Log.d("Add Profile", "onResponse: " + result.toString());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());
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
            // Adding request to request queue
            Log.d("Address Put", "request: " + changeProfile.toString());
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(changeProfile);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setGenreList() {
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, Helper.baseURL + "/genres", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Volley Response", "response: " + response.toString());
                int length = response.length();
                if (length > 0) {
                    GENRES.clear();
                    GENRES.add("Genre");
                    Log.d("LIST", "cleared: " + GENRES.toString());
                }
                for (int i = 0; i < length; i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String genre = object.getString("genre");
                        GENRES.add(genre);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                setGenreSpinner();
                autofill();
                Log.d("LIST", "onResponse: " + GENRES.toString());
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
        Log.d("GET", "genre request: " + jsonArrayReq.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq);
    }

    private void setGenreSpinner() {
        int size = GENRES.size();
        String[] genreTempArray = new String[size];
        for (int i = 0; i < size; i++) {
            genreTempArray[i] = GENRES.get(i);
        }
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genreTempArray);
        genreAdapter.setDropDownViewResource(R.layout.dropdown_item);
        genreDropdown.setAdapter(genreAdapter);
    }

    private void autofill() {
        String url = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType.toLowerCase() + "s/" + UserInfo.getInstance().loggedInUser.id;
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Edit", "response: " + response.toString());
                //name
                try {
                    if (response.has("name")) {
                        if (!response.getString("name").equals("null")){
                            enterTitle.setHint(response.getString("name"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (response.has("image")) {
                        LoadImage loader = new LoadImage(imagePreview);
                        loader.execute(response.getString("image"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //genre
                try {
                    if (response.has("genre")) {
                        String genre = response.getString("genre");
                        int index = GENRES.indexOf(genre);
                        if (index != -1 && index < GENRES.size()) {
                            genreDropdown.setSelection(index);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //description
                try {
                    if (response.has("description")) {
                        if (!response.getString("description").equals("null")){
                            enterDescription.setHint(response.getString("description"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //address
                try {
                    if (response.has("address")) {
                        JSONObject address = response.getJSONObject("address");
                        if (address.has("streetAddress")) {
                            if (!address.getString("streetAddress").equals("null")){
                                enterStreetAddress.setHint(address.getString("streetAddress"));
                            }
                        }
                        if (address.has("city")) {
                            if (!address.getString("city").equals("null")){
                                enterCity.setHint(address.getString("city"));
                            }
                        }
                        if (address.has("state")) {
                            String state = address.getString("state");
                            int index = stateAdapter.getPosition(state);
                            if (index != -1 && index < stateAdapter.getCount()) {
                                stateDropdown.setSelection(index);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                currentProfile = response;
                Log.d("edit autofill", "current profile: " + currentProfile);
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

    private void showFields() {
        if (hasImage) {
            imageWrapper.setVisibility(View.VISIBLE);
        } else {
            imageWrapper.setVisibility(View.GONE);
        }

        if (hasTitle) {
            titleWrapper.setVisibility(View.VISIBLE);
        } else {
            titleWrapper.setVisibility(View.GONE);
        }

        if (hasGenre) {
            genreWrapper.setVisibility(View.VISIBLE);
        } else {
            genreWrapper.setVisibility(View.GONE);
        }

        if (hasStreetAddress) {
            streetWrapper.setVisibility(View.VISIBLE);
        } else {
            streetWrapper.setVisibility(View.GONE);
        }

        if (hasCity) {
            cityWrapper.setVisibility(View.VISIBLE);
        } else {
            cityWrapper.setVisibility(View.GONE);
        }

        if (hasState) {
            stateWrapper.setVisibility(View.VISIBLE);
        } else {
            stateWrapper.setVisibility(View.GONE);
        }

        if (hasDescription) {
            descriptionWrapper.setVisibility(View.VISIBLE);
        } else {
            descriptionWrapper.setVisibility(View.GONE);
        }
    }

    private void uploadImage(int elementID){

        String imgURL = Helper.baseURL + "/images";
        Log.d("upload image", imgURL);
        if (selectiedUri == null){
            return;
        }

        byte[] imageData = convertImageUriToBytes(selectiedUri);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, imgURL, imageData, response -> {
            addImage(elementID, response);
        },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private void addImage(int elementID, String img) {
        String addImgURL = Helper.baseURL + "/" + UserInfo.getInstance().loggedInUser.loginInfo.userType + "s/" + elementID;

        Log.d("addImg to concert", "addImg: " + addImgURL);

        JSONObject imageParam = new JSONObject();
        try{
            int imgId = Integer.parseInt(img);
            imageParam.put("image", Helper.baseURL + "/images/" + imgId);

        } catch (Exception e) {
            Log.d("addImage", "not an int: " + img);
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, addImgURL, imageParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                Log.d("Add venue", "onResponse: " + result.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
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
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    private void uploadImage() {
        byte[] imageData = convertImageUriToBytes(selectiedUri);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                UPLOAD_URL,
                imageData,
                response -> {
                    // Handle response
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    Log.d("Upload", "Response: " + response);
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}