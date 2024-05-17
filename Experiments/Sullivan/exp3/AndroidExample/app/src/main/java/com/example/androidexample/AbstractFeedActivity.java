package com.example.androidexample;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFeedActivity extends AppCompatActivity {
    // attributes for class
    boolean priceAttribute;
    boolean favoriteAttribute;
    boolean chatAttribute;
    boolean  createAttribute;

    // UI elements
    ScrollView scrollView;
    LinearLayout scrollable;
    ImageView profileButton;
    ImageView banner;
    ImageView nextButton;
    ImageView prevButton;
    TextView pageNumberDisplay;
    ImageView menuButton;
    LinearLayout dropdownMenu;

    //menu icons
    ImageView favoriteIcon, chatIcon, createIcon, mapIcon;

    // cards
    View card0, card1, card2, card3, card4 ,card5, card6, card7, card8, card9;

    // card info
    TextView cardName0, cardName1, cardName2, cardName3, cardName4 ,cardName5, cardName6, cardName7, cardName8, cardName9;
    TextView cardPrice0, cardPrice1, cardPrice2, cardPrice3, cardPrice4, cardPrice5 ,cardPrice6, cardPrice7, cardPrice8, cardPrice9;
    ImageView cardImage0, cardImage1, cardImage2, cardImage3, cardImage4, cardImage5, cardImage6 ,cardImage7, cardImage8, cardImage9;

    //list of elements
    ArrayList<Element> elements = new ArrayList<Element>();

    //track what page
    int page;

    //hold url to access elements
    String arrayURL = "https://a7305e71-6430-4473-92f1-0341186ca1bc.mock.pstmn.io";


    //screen start up procedure/hierarchy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* initialize UI elements */
        setContentView(R.layout.activity_feed);             // link to feed activity XML
        abstractInitUI();
    }
    @Override
    protected void onStart() {
        super.onStart();

        //hide dropdown
        dropdownMenu.setVisibility(View.GONE);

        // get page
        if (getIntent().hasExtra("PAGE")){
            String pageString = getIntent().getExtras().getString("PAGE");

            //set page
            page =  0;
            if (pageString != null){
                page = Integer.parseInt(pageString);
            }
        }

        /* add elements from server */
        createList();

        /* set screen buttons */
        setUIListeners();

        /* click listeners for cards */
        setCardListeners();

        Log.d("onStart", "complete ");

    }
    @Override
    protected void onResume() {
        super.onResume();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0, 0);
        Log.d("onResume", "complete ");
    }

    //helper methods
    void abstractInitUI(){
        //UI elements
        scrollView = findViewById(R.id.scrollView);
        scrollable = findViewById(R.id.scrollable);
        profileButton = findViewById(R.id.profileButton);
        banner = findViewById(R.id.banner);
        nextButton = findViewById(R.id.nextPage);
        prevButton = findViewById(R.id.prevPage);
        pageNumberDisplay = findViewById(R.id.pageNumberDisplay);
        menuButton = findViewById(R.id.menuIcon);
        dropdownMenu = findViewById(R.id.dropdownMenu);

        //menu
        favoriteIcon = findViewById(R.id.favoriteIcon);
        chatIcon = findViewById(R.id.chatIcon);
        createIcon = findViewById(R.id.createIcon);
        mapIcon = findViewById(R.id.mapIcon);

        //cards
        card0 = findViewById(R.id.card0);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);

        //card 0
        cardName0 = findViewById(R.id.card0Name);
        cardImage0 = findViewById(R.id.card0Image);
        //card 1
        cardName1 = findViewById(R.id.card1Name);
        cardImage1 = findViewById(R.id.card1Image);
        //card 2
        cardName2 = findViewById(R.id.card2Name);
        cardImage2 = findViewById(R.id.card2Image);
        //card 3
        cardName3 = findViewById(R.id.card3Name);
        cardImage3 = findViewById(R.id.card3Image);
        //card 4
        cardName4 = findViewById(R.id.card4Name);
        cardImage4 = findViewById(R.id.card4Image);
        //card 5
        cardName5 = findViewById(R.id.card5Name);
        cardImage5 = findViewById(R.id.card5Image);
        //card 6
        cardName6 = findViewById(R.id.card6Name);
        cardImage6 = findViewById(R.id.card6Image);
        //card 7
        cardName7 = findViewById(R.id.card7Name);
        cardImage7 = findViewById(R.id.card7Image);
        //card 8
        cardName8 = findViewById(R.id.card8Name);
        cardImage8 = findViewById(R.id.card8Image);
        //card 9
        cardName9 = findViewById(R.id.card9Name);
        cardImage9 = findViewById(R.id.card9Image);

        initUI();

    }
    void setUIListeners(){
        /* click listener for buttons */
        //menu buttons
        if (favoriteAttribute){
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, GreeterActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            favoriteIcon.setVisibility(View.GONE);
        }

        if (chatAttribute){
            chatIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, GreeterActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            chatIcon.setVisibility(View.GONE);
        }

        if (createAttribute){
            createIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AbstractFeedActivity.this, GreeterActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            createIcon.setVisibility(View.GONE);
        }

        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbstractFeedActivity.this, RadiusActivity.class);
                startActivity(intent);
            }
        });


        //button to go to logout
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbstractFeedActivity.this, GreeterActivity.class);
                startActivity(intent);
            }
        });

        /* click listener for buttons */
        //button to go to logout
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* toggle drop down menu */
                if (dropdownMenu.getVisibility() == View.VISIBLE){
                    dropdownMenu.setVisibility(View.GONE);
                } else if (dropdownMenu.getVisibility() == View.GONE){
                    dropdownMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        // banner
        initBanner();

        //previous button
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page--;
                createList();
                setCardListeners();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                scrollView.smoothScrollTo(0, 0);
            }
        });

        //next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                createList();
                setCardListeners();
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }
    void setCardListeners(){
        //card 0
        setOneCardListener(card0, 0);

        //card 1
        setOneCardListener(card1, 1);

        //card 2
        setOneCardListener(card2, 2);

        //card 3
        setOneCardListener(card3, 3);

        //card 4
        setOneCardListener(card4, 4);

        //card 5
        setOneCardListener(card5, 5);

        //card 6
        setOneCardListener(card6, 6);

        //card 7
        setOneCardListener(card7, 7);

        //card 8
        setOneCardListener(card8, 8);

        //card 9
        setOneCardListener(card9, 9);

    }
    void setCards(){

        int items = elements.size();

        //card 0
        setOneCard(items, 0, card0, cardName0, cardPrice0, cardImage0);

        //card 1
        setOneCard(items, 1, card1, cardName1, cardPrice1, cardImage1);

        //card 2
        setOneCard(items, 2, card2, cardName2, cardPrice2, cardImage2);

        //card 3
        setOneCard(items, 3, card3, cardName3, cardPrice3, cardImage3);

        //card 4
        setOneCard(items, 4, card4, cardName4, cardPrice4, cardImage4);

        //card 5
        setOneCard(items, 5, card5, cardName5, cardPrice5, cardImage5);

        //card 6
        setOneCard(items, 6, card6, cardName6, cardPrice6, cardImage6);

        //card 7
        setOneCard(items, 7, card7, cardName7, cardPrice7, cardImage7);

        //card 8
        setOneCard(items, 8, card8, cardName8, cardPrice8, cardImage8);

        //card 9
        setOneCard(items, 9, card9, cardName9, cardPrice9, cardImage9);

        //set buttons
        setButtons(elements.size(), page);
    }
    void setOneCard(int items, int index, View card, TextView name, TextView price, ImageView image){

        if (index >= (items)){
            card.setVisibility(View.GONE);
        }  else {
            Element element = elements.get(index);
            name.setText(element.name);

            if (element.hasImage()){
                setImageURL(element.image, image);
                scaleImage(image);
            } else {
                image.setImageResource(R.drawable.blank);
            }

            if (element.hasPrice() && priceAttribute){
                price.setText(element.price);
            }
            card.setVisibility(View.VISIBLE);
        }
    }
    void setButtons(int size, int page){
        pageNumberDisplay.setText(String.valueOf(page + 1));

        if (size < 11){
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }

        if (page <= 0){
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }
    }
    void setImageURL(String url, ImageView imageView){
        LoadImage loader = new LoadImage(imageView);
        loader.execute(url);
    }
    void createList() {
        Log.d("Array URL", "createList: " + arrayURL);
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.GET, arrayURL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        elements.clear();

                        // Parse the JSON array and add data to the adapter
                        for (int i = (page * 10); (i < response.length()) && (i < ((page + 1) * 10 + 1)); i++) {
                            try {
                                //get json object
                                JSONObject jsonObject = response.getJSONObject(i);

                                //get name to add
                                String name = null;
                                if (jsonObject.has("name")){
                                    name = jsonObject.getString("name");
                                }

                                //create element to add
                                Element element = new Element(name);

                                //try adding each attribute
                                addAttributes(jsonObject, element);

                                elements.add(element);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //set cards after elements have been added (must be here or unexpected behavior due to load times)
                        setCards();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
    void scaleImage(ImageView cardImage) {
        int layoutWidth = Resources.getSystem().getDisplayMetrics().widthPixels  - 6 * (scrollable.getPaddingStart() + scrollable.getPaddingEnd());
        int imgWidth = cardImage.getDrawable().getIntrinsicWidth();
        int imgHeight = cardImage.getDrawable().getIntrinsicHeight();
        float ratio = imgHeight / ((float) imgWidth);
        cardImage.getLayoutParams().height = ((int) (layoutWidth * ratio * 1.2));
        cardImage.requestLayout();
    }



    abstract void setOneCardListener(View card, int index);
    abstract void initUI();
    abstract void setExtras(Intent intent, Element element);
    abstract void addAttributes(JSONObject jsonObject, Element element);
    abstract void initBanner();
}