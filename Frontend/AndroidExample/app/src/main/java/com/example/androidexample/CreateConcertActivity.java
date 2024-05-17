package com.example.androidexample;

import static android.text.TextUtils.concat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Allows a venue to create a concert. Binds the created concert to their account (backend).
 */
public class CreateConcertActivity extends AppCompatActivity {

    private ImageView backButton;  // define username textview variable
    private View postButton;


    private View.OnKeyListener keyListener;

    private EditText enterTitle, enterPrice, enterDescription;
    private TextView enterDate, enterTime, enterBands;
    private DatePicker chooseDate;
    private TimePicker chooseTime;

    private LinearLayout bandSearchContainer;
    private SearchView bandSearch;
    private ListView bandView, selectedBandView;
    private ArrayList<Band> bands, selectedBands;
    private CaseInsensitiveArrayAdapter bandAdapter, selectedBandAdapter;

    private LinearLayout enterImage;
    private ImageView imagePreview;
    private Spinner genreDropdown, agesDropdown;
    private CheckBox freeBox;
    private Boolean priceEmpty = true;

    private Element createdConcert;

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String[] AGES = {"Ages", "All Ages", "12+", "16+", "18+", "21+", "Other"};
    ArrayList<String> GENRES = new ArrayList<String>();

    private ActivityResultLauncher<String> getImage;
    Uri selectiedUri;

    String arrayURL = Helper.baseURL + "/concerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_concert);

        /* initialize UI elements */
        initUI();

        /* set listeners */
        setListeners();
    }

    private void initUI(){
        //connect to xml
        backButton = findViewById(R.id.backButton);
        postButton = findViewById(R.id.postButton);

        enterImage = findViewById(R.id.enterImage);
        imagePreview = findViewById(R.id.imagePreview);
        enterTitle = findViewById(R.id.enterTitle);
        enterBands = findViewById(R.id.enterBands);
        enterTime = findViewById(R.id.enterTime);
        enterPrice = findViewById(R.id.enterPrice);
        enterPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        enterDescription = findViewById(R.id.enterDescription);

        freeBox = findViewById(R.id.freeBox);

        enterDate = findViewById(R.id.enterDate);
        chooseDate = findViewById(R.id.chooseDate);

        enterTime = findViewById(R.id.enterTime);
        chooseTime = findViewById(R.id.chooseTime);

        bandSearchContainer = findViewById(R.id.bandSearchContainer);
        bandSearch = findViewById(R.id.bandSearch);
        bandView = findViewById(R.id.bandList);
        selectedBandView = findViewById(R.id.bandSelectedList);

        /* set genre spinner */
        genreDropdown = findViewById(R.id.genreDropdown);
        //default
        GENRES.add("Genre");
        GENRES.add("Rock");
        GENRES.add("Country");
        GENRES.add("Rap");
        setGenreSpinner();

        //attempt to load from server
        setGenreList();

        /* set ages spinner */
        agesDropdown = findViewById(R.id.agesDropdown);
        ArrayAdapter<String> agesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AGES);
        agesAdapter.setDropDownViewResource(R.layout.dropdown_item);
        agesDropdown.setAdapter(agesAdapter);

        /* set pickers */
        chooseTime.setVisibility(View.GONE);
        chooseDate.setVisibility(View.GONE);
        chooseDate.setMinDate(System.currentTimeMillis() - 1000);

        /* register image picker */
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri != null) {
                        selectiedUri = uri;
                        imagePreview.setImageURI(uri);
                    }
                });

        /* search for bands */
        bandSearchContainer.setVisibility(View.GONE);
        bands = new ArrayList<>();
        selectedBands = new ArrayList<>();

        bandAdapter = new CaseInsensitiveArrayAdapter(CreateConcertActivity.this, R.layout.custom_listview_item, bands);
        selectedBandAdapter = new CaseInsensitiveArrayAdapter(CreateConcertActivity.this, R.layout.custom_listview_item, selectedBands);

        bandView.setAdapter(bandAdapter);
        selectedBandView.setAdapter(selectedBandAdapter);

        addBands();
    }

    private void setListeners(){
        // hides keyboard on enter
        keyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //enter key code
                    //hide keyboard
                    Helper.hideKeyboard(CreateConcertActivity.this);

                    //exit
                    return true;
                }
                return false;
            }
        };

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // pickers
        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseDate.getVisibility() == View.GONE) {
                    chooseDate.setVisibility(View.VISIBLE);
                    chooseTime.setVisibility(View.GONE);
                } else if (chooseDate.getVisibility() == View.VISIBLE) {
                    chooseDate.setVisibility(View.GONE);
                }
            }
        });
        chooseDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                enterDate.setText(MONTHS[monthOfYear] + " " + dayOfMonth + ", " + year);
                String chosenDate = "";
                if (monthOfYear < 10){
                    chosenDate += "0" + monthOfYear;
                } else {
                    chosenDate += monthOfYear;
                }

                if (dayOfMonth < 10){
                    chosenDate += "/0" + dayOfMonth;
                } else {
                    chosenDate += "/" + dayOfMonth;
                }

                if (year > 999){
                    chosenDate += "/" + year;
                } else if (year > 99){
                    chosenDate += "/0" + year;
                } else if (year > 9){
                    chosenDate += "/00" + year;
                } else {
                    chosenDate += "/000" + year;
                }
            }
        });
        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseTime.getVisibility() == View.GONE) {
                    chooseDate.setVisibility(View.GONE);
                    chooseTime.setVisibility(View.VISIBLE);
                } else if (chooseTime.getVisibility() == View.VISIBLE) {
                    chooseTime.setVisibility(View.GONE);
                }
            }
        });
        chooseTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
                String time = formatter.format(LocalTime.of(hourOfDay, minute));
                enterTime.setText(time);
            }
        });

        //enter fields
        enterImage.setOnClickListener(v -> getImage.launch("image/*"));

        enterTitle.setOnKeyListener(keyListener);
        enterTime.setOnKeyListener(keyListener);
        enterPrice.setOnKeyListener(keyListener);
        enterDescription.setOnKeyListener(keyListener);

        enterBands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bandSearchContainer.getVisibility() == View.GONE){
                    bandSearchContainer.setVisibility(View.VISIBLE);
                } else {
                    bandSearchContainer.setVisibility(View.GONE);
                }
            }
        });

        enterPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                priceEmpty = s.toString().equals("Free");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0 && priceEmpty) {
                    return;
                }

                if (s.toString().equals("$")) {
                    Selection.setSelection(enterPrice.getText(), enterPrice.getText().length());
                    return;
                }

                if (s.toString().equals("$$")) {
                    enterPrice.setText("$");
                    Selection.setSelection(enterPrice.getText(), enterPrice.getText().length());
                    return;
                }

                if (s.toString().length() == 1) {
                    enterPrice.setText("$" + s);
                    Selection.setSelection(enterPrice.getText(), enterPrice.getText().length());
                    return;
                }

                if (!s.toString().startsWith("$") && !s.toString().equals("Free")) {
                    enterPrice.setText("$");
                    Selection.setSelection(enterPrice.getText(), enterPrice.getText().length());
                }

            }
        });
        freeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KeyListener keyListener = enterPrice.getKeyListener();
                if (isChecked) {
                    enterPrice.setText("Free");
                    enterPrice.setEnabled(false);
                } else {
                    enterPrice.setText("");
                    enterPrice.setEnabled(true);
                }
            }
        });

        bandSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bandAdapter.getFilter().filter(query);

                if (query.length() != 0){
                    selectedBandView.setVisibility(View.GONE);
                } else {
                    selectedBandView.setVisibility(View.VISIBLE);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bandAdapter.getFilter().filter(newText);

                if (newText.length() != 0){
                    selectedBandView.setVisibility(View.GONE);
                } else {
                    selectedBandView.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

        bandAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Band band = (Band) bandView.getItemAtPosition(position);
                Log.d("bandview", band.toString());

                bands.remove(band);
                Log.d("bands after remove", bands.toString());
                Helper.setListViewSize(bandView);

                selectedBands.add(band);
                Helper.setListViewSize(selectedBandView);

                updateBandText();
            }
        });

        selectedBandAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Band band = (Band) selectedBandView.getItemAtPosition(position);

                selectedBands.remove(band);
                Helper.setListViewSize(selectedBandView);

                bands.add(band);
                Log.d("bands after remove", bands.toString());
                Helper.setListViewSize(bandView);

                updateBandText();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createdConcert = new Element(String.valueOf(enterTitle.getText()));

                //time
                if (enterTime.getText() != null) {
                    createdConcert.addDescription(String.valueOf(enterTime.getText()));
                }

                //date
                if (enterDate.getText() != null) {
                    createdConcert.addDate(String.valueOf(enterDate.getText()));
                }

                //price
                if (enterPrice.getText() != null && !enterPrice.toString().equals("$")) {
                    createdConcert.addPrice(String.valueOf(enterPrice.getText()));
                }

                //genre
                if (!genreDropdown.getSelectedItem().toString().equals("Genre")) {
                    createdConcert.addGenre(genreDropdown.getSelectedItem().toString());
                }

                //ages
                if (!agesDropdown.getSelectedItem().toString().equals("Ages")) {
                    createdConcert.addAges(agesDropdown.getSelectedItem().toString());
                }

                //description
                if (enterDescription.getText() != null) {
                    createdConcert.addDescription(String.valueOf(enterDescription.getText()));
                }

                createConcert(createdConcert);
                finish();
            }
        });
    }

    private void createConcert(Element concert) {
        Log.d("Create Concert", "createList: " + arrayURL);
        JSONObject obj = new JSONObject();
        try {
            if (concert.name != null) {
                obj.put("name", concert.name);
            }

            if (concert.date != null) {
                obj.put("date", concert.date);
            }

            if (concert.hasImage()) {
                obj.put("image", concert.image);
            }

            if (concert.price != null) {
                obj.put("price", concert.price);
            }

            if (concert.genre != null) {
                obj.put("genre", concert.genre);
            }

            if (concert.description != null) {
                obj.put("description", concert.description);
            }

            if (concert.ages != null) {
                obj.put("ages", concert.ages);
            }

            if (concert.bands != null) {
                obj.put("bands", concert.bands);
            }

        } catch (Exception e) {
            Log.d("Create Concert", "failed");
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, arrayURL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Create concert", "onResponse: " + response.toString());
                if (response.has("id")) {
                    try {
                        Log.d("Create concert", "id: " + response.toString());
                        if (UserInfo.getInstance().loggedInUser != null) {
                            int concertId = Integer.parseInt(response.getString("id"));
                            addVenue(concertId);
                            uploadImage(concertId);
                            //bands
                            if (enterBands.getText() != null) {
                                int length = selectedBands.size();
                                for (int i = 0; i < length; i++){
                                    addBandToConcert(concertId, selectedBands.get(i).id);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
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
//                headers.put("Content-Type", "application/json");
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

    private void addVenue(int concertId) throws JSONException {
        String addVenueURL = Helper.baseURL + "/venues/" + UserInfo.getInstance().loggedInUser.id + "/concerts/" + concertId;

        Log.d("ADD VENUE", "addVenue: " + addVenueURL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, addVenueURL, null, new Response.Listener<JSONObject>() {
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
        Log.d("GET", "onClick: " + jsonArrayReq.toString());

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

    private void addBands() {
        JsonArrayRequest getCards = new JsonArrayRequest(Request.Method.GET, Helper.baseURL + "/bands", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("addBands()", "response: " + response.toString());
                int length = response.length();
                for (int i = 0; i < length; i++) {
                    try {
                        Gson gson = new Gson();
                        Band band = gson.fromJson(response.getJSONObject(i).toString(), Band.class);
                        bands.add(band);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setBandView();
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
        Log.d("GET", "get cards: " + getCards.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(getCards);
    }

    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
    private void uploadImage(int concertId){

        String imgURL = Helper.baseURL + "/images";
        Log.d("upload image", imgURL);
        if (selectiedUri == null){
            return;
        }

        byte[] imageData = convertImageUriToBytes(selectiedUri);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, imgURL, imageData, response -> {
                   addImage(concertId, response);
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }
    private void addImage(int concertId, String img) {
        String addImgURL = arrayURL + "/" + concertId;

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
    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
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

    private void updateBandText(){
        int length = selectedBands.size();

        if (length == 0){
            enterBands.setText("");
            return;
        }

        if (length == 1){
            enterBands.setText(selectedBands.get(0).name);
            return;
        }

        String chosenBandText = "";
        int i;
        for (i = 0; i < length - 1; i++){
            chosenBandText += " " + selectedBands.get(i).name + ",";
        }
        chosenBandText += " and " + selectedBands.get(i).name;

        enterBands.setText(chosenBandText);
    }

    private void addBandToConcert(int concertId, int bandId){
        String addVenueURL = Helper.baseURL + "/concerts/" + concertId + "/bands/" + bandId;
        Log.d("ADD VENUE", "addVenue: " + addVenueURL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, addVenueURL, null, new Response.Listener<JSONObject>() {
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

    private void setBandView(){
        bandAdapter = new CaseInsensitiveArrayAdapter(CreateConcertActivity.this, R.layout.custom_listview_item, bands);
        bandAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Band band = (Band) bandView.getItemAtPosition(position);
                Log.d("bandview", band.toString());

                bands.remove(band);
                setBandView();

                selectedBands.add(band);
                setSelectedBandView();

                updateBandText();

                bandSearch.setQuery("", false);
                bandSearch.clearFocus();
            }
        });
        bandView.setAdapter(bandAdapter);

        Helper.setListViewSize(bandView);
    }

    private void setSelectedBandView(){
        selectedBandAdapter = new CaseInsensitiveArrayAdapter(CreateConcertActivity.this, R.layout.custom_listview_item, selectedBands);
        selectedBandAdapter.setOnItemClickListener(new CaseInsensitiveArrayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Band band = (Band) selectedBandView.getItemAtPosition(position);
                Log.d("bandview", band.toString());

                selectedBands.remove(band);
                setSelectedBandView();
                setBandView();

                bands.add(band);
                setBandView();

                updateBandText();
            }
        });
        selectedBandView.setAdapter(selectedBandAdapter);

        Helper.setListViewSize(selectedBandView);
    }

}