package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminHomeActivity extends AppCompatActivity {
    private ImageView userViews;
    private LinearLayout dropdownMenu;
    private ImageView attendeeView, bandView, venueView, profile;
    private TextView attendeeNum, bandNum, venueNum, concertNum, ticketNum, orderNum, incomeNum, field, avg, field1, field3,field4, field5;
    private Button attendeeList, bandList, venueList, postGenres, delUsers, delConcert, submit0,submit1, submit2;
    private ConstraintLayout form, form1, form2;
    private EditText boxEntry, box1, box3, box5;
    private Spinner userCat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        dropdownMenu = findViewById(R.id.dropdownMenu);
        userViews = findViewById(R.id.menuIcon);
        dropdownMenu.setVisibility(View.GONE);
        attendeeView = findViewById(R.id.attendeeView);
        bandView = findViewById(R.id.bandView);
        venueView = findViewById(R.id.venueView);
        attendeeNum = findViewById(R.id.attendee_num);
        bandNum = findViewById(R.id.band_num);
        venueNum = findViewById(R.id.venue_num);
        concertNum = findViewById(R.id.concert_num);
        ticketNum = findViewById(R.id.ticket_num);
        orderNum = findViewById(R.id.order_num);
        incomeNum = findViewById(R.id.income_num);
        attendeeList = findViewById(R.id.attendee_list);
        bandList = findViewById(R.id.band_list);
        venueList = findViewById(R.id.venue_list);
        postGenres = findViewById(R.id.add_genres);
        delUsers = findViewById(R.id.del_users);
        delConcert = findViewById(R.id.del_concert);
        form = findViewById(R.id.fillForm);
        field = findViewById(R.id.header);
        boxEntry = findViewById(R.id.field);
        submit0 = findViewById(R.id.submit);
        avg = findViewById(R.id.avg_tickets);
        submit1 = findViewById(R.id.submit0);
        form1 = findViewById(R.id.fillForm1);
        field1 = findViewById(R.id.header1);
        userCat = findViewById(R.id.spinner0);
        box1 = findViewById(R.id.field1);
        box3 = findViewById(R.id.field3);
        box5 = findViewById(R.id.field10);
        field3 = findViewById(R.id.header3);
        field5 = findViewById(R.id.header30);
        submit2 = findViewById(R.id.submit2);
        form2 = findViewById(R.id.fillForm2);
        profile = findViewById(R.id.profileButton);
        form.setVisibility(View.GONE);
        form1.setVisibility(View.GONE);
        form2.setVisibility(View.GONE);

        String [] userArray = {"Select Category", "Attendee", "Band", "Venue"};
        ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userArray);
        userCat.setAdapter(userAdapter);

        userViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* toggle drop down menu */
                if (dropdownMenu.getVisibility() == View.VISIBLE) {
                    dropdownMenu.setVisibility(View.GONE);
                } else if (dropdownMenu.getVisibility() == View.GONE) {
                    dropdownMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminProfileActivity.class);
                startActivity(intent);
            }
        });

        attendeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeAttendees = new Intent(AdminHomeActivity.this, AttendeeListActivity.class);
                startActivity(seeAttendees);
            }
        });

        bandList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeAttendees = new Intent(AdminHomeActivity.this, BandListActivity.class);
                startActivity(seeAttendees);
            }
        });

        venueList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeAttendees = new Intent(AdminHomeActivity.this, VenueListActivity.class);
                startActivity(seeAttendees);
            }
        });

        postGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Button Click", "True");
                form.setVisibility(View.VISIBLE);
                field.setText(" Genre");
                boxEntry.setHint("  Enter Genre");
            }
        });

        submit0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Submit", "Clicked");
                final String genreEntered = boxEntry.getText().toString();

                if (!genreEntered.isEmpty()) {
                    Map<String, String> params = new HashMap<>();
                    params.put("genre", genreEntered);

                    // Replace the URL with your server's URL
                    final String postGenreURL = "http://coms-309-017.class.las.iastate.edu:8080/genres";

                    JSONObject jsonObject = new JSONObject(params);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postGenreURL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                String message = jsonObject.getString("message");
                                Log.i("Genre Add Status Message", message);
                                Toast.makeText(AdminHomeActivity.this, "Genre Added Successfully", Toast.LENGTH_SHORT).show();
                                form.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            volleyError.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            // Add any headers if required
                            return headers;
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            // You can remove this method as you are sending the parameters in the body
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });

        delUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct URL for DELETE request
                Log.i("Button Click", "True");
                form1.setVisibility(View.VISIBLE);
                field1.setText("Category ID");
                box1.setHint("Enter Category User ID");
            }
        });

        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Submit", "Clicked");
                String catId = box1.getText().toString();
                String url = Helper.baseURL + "/" + userCat.getSelectedItem().toString().toLowerCase()+"s/" + catId;
                // Create DELETE request using Volley
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On successful deletion, display toast message and redirect to AdminScreen
                        Toast.makeText(AdminHomeActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });
                // Add request to Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
                form1.setVisibility(View.GONE);
            }
        });


        delConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Construct URL for DELETE request
                Log.i("Button Click", "True");
                form2.setVisibility(View.VISIBLE);
                field3.setText("Venue ID");
                field5.setText("Concert ID");
                box3.setHint("Enter Venue ID");
                box5.setHint("Enter Concert ID");
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Submit", "Clicked");
                String catId = box3.getText().toString();
                String conId = box5.getText().toString();
                String deleteConcertURL = Helper.baseURL + "/venues/" + catId + "/concerts/" + conId;
                // Create DELETE request using Volley
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, deleteConcertURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // On successful deletion, display toast message and redirect to AdminScreen
                        Toast.makeText(AdminHomeActivity.this, "Concert Deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                    }
                });
                // Add request to Volley request queue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
                form2.setVisibility(View.GONE);
            }
        });

//        final String numUsersURL = "http://coms-309-017.class.las.iastate.edu:8080/numberofusers";
        final String numUsersURL = "http://coms-309-017.class.las.iastate.edu:8080/numberofusers";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, numUsersURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Message got back",response.toString());
                    int numAttendees = response.getInt("Attendees");
                    int numBands = response.getInt("Bands");
                    int numVenues = response.getInt("Venues");
                    int numConcerts = response.getInt("Concerts");

                    String convertA = String.valueOf(numAttendees);
                    String convertB = String.valueOf(numBands);
                    String convertV = String.valueOf(numVenues);
                    String convertC = String.valueOf(numConcerts);

                    attendeeNum.setText(convertA);
                    bandNum.setText(convertB);
                    venueNum.setText(convertV);
                    concertNum.setText(convertC);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminHomeActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);


//        final String financeUrl = "http://coms-309-017.class.las.iastate.edu:8080/orderdetails";
        final String financeUrl = "http://coms-309-017.class.las.iastate.edu:8080/orderdetails";

        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET, financeUrl, null, new Response.Listener<JSONObject>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Message got back", response.toString());
                    String numOrders = response.getString("Orders");
                    String totalIncome = response.getString("Total Income");
                    String numTickets = response.getString("Number of Tickets Bought");
                    String avgTickets = response.getString("Average Tickets Per Order");

                    orderNum.setText(numOrders);
                    ticketNum.setText(numTickets);
                    incomeNum.setText("$" + String.format("%.2f", Double.parseDouble(totalIncome)));
                    avg.setText(String.format("%.2f", Double.parseDouble(avgTickets)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminHomeActivity.this, "No Finances Found", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq2);

        attendeeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 75;
                String url = Helper.baseURL + "/attendees/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Attendee.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(AdminHomeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminHomeActivity.this, FanFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminHomeActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        venueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 21;
                String url = Helper.baseURL + "/venues/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Venue.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(AdminHomeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminHomeActivity.this, VenueFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminHomeActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

        /* click listener on band button pressed */
        bandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user to login as
                int userID = 25;
                String url = Helper.baseURL + "/bands/" + userID;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User loggedInUser;
                            Gson gson = new Gson();

                            loggedInUser = gson.fromJson(response.toString(), Band.class);
                            if (loggedInUser != null) {
                                UserInfo.getInstance().loggedInUser = loggedInUser;
                            }
                            Toast.makeText(AdminHomeActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminHomeActivity.this, BandFeedActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminHomeActivity.this, "User not found with the given email and password! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });
    }
}
