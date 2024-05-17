// ReviewActivity.java

package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ReviewActivity represents an activity in the Android application that displays reviews and ratings.
 * It establishes a WebSocket connection to receive real-time updates of reviews and ratings from a server.
 * Users can view the average rating, individual ratings with feedback, and corresponding star images.
 */
public class ReviewActivity extends AppCompatActivity implements WebSocketListener {
    /**
     * Represents a TextView to display the average rating.
     */
    private TextView numStarsAvg;
    /**
     * Represents an ImageView to display stars.
     */
    private ImageView stars;
    /**
     * Manages WebSocket connections for the application.
     */
    private WebSocketManager wsm;
    /**
     * Represents a ListView to display a list of reviews and ratings.
     */
    ListView list;
    /**
     * Stores a list of ratings associated with reviews, to calculate the average ratings.
     */
    ArrayList<Float> ratings;
    /**
     * Stores a list of reviews and ratings, that will be displayed on the screen.
     */
    ArrayList<String> reviews;
    private ImageView back;
    /**
     * Manages the data of the ListView by adapting an ArrayList of strings.
     */
    ArrayAdapter<String> arrayAdapter;
    private int concert_id;
    private String id;

    /**
     * This is the lifecycle method, that initializes variables and sets the screen for the user to view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbackforallusers);
        concert_id = getIntent().getIntExtra("WSsessionID", -1);
        Log.i("ID Received from Element", String.valueOf(concert_id));
        numStarsAvg = findViewById(R.id.numStarsAverage);
        stars = findViewById(R.id.numStarsImage);
        list = findViewById(R.id.list);
        ratings = new ArrayList<>();
        reviews = new ArrayList<>();
        back = findViewById(R.id.back);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);
        list.setAdapter(arrayAdapter);
        id = String.valueOf(concert_id);

        // Prepare WebSocket link with username. Use of array, to split the user's name if in the form of First & Last
        String user = "";
        if (UserInfo.getInstance().loggedInUser != null) {
            user = UserInfo.getInstance().loggedInUser.name;
        } else {
            Log.e("ERROR",user);
            user = "Admin";
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] nameSplit = user.split("");
        String name;
        if(nameSplit.length == 2) {
            name = nameSplit[1];
        } else {
            name = nameSplit[0];
        }
        // Websocket Link
        String wsLink = "ws://coms-309-017.class.las.iastate.edu:8080/review/" + name +"/"+ id;
        Log.i("WS Link", wsLink);

        // Establish WebSocket connection
        wsm = WebSocketManager.getInstance();
        wsm.setWebSocketListener(this);
        wsm.connectWebSocket(wsLink);
    }

    /**

     Method called when a WebSocket connection is opened.
     This method should be overridden to define custom behavior when a WebSocket connection is established.
     @param handshakedata The details of the handshake data associated with the WebSocket connection.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     * Callback method invoked when a message is received over the WebSocket connection.
     * This method ensures that the UI-related operation (adding a review to the screen) is executed on the main UI thread, spontaneously.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            Log.d("DEBUG","inside onWebSocketMessage");
            addReviewToScreen(message);
        });

    }

    /**
     * Adds a review message to the screen.
     *
     * @param message the review message containing the rating and feedback
     */

    private void addReviewToScreen(String message) {
        String msg = message.replace("Rating: ", "");
        Log.d("MESSAGE RECORDED", msg);
        int index = msg.indexOf("\n");

        // Check if the newline character is present in the message
        if (index == -1) {
            Log.e("ReviewActivity", "Message format error: newline character not found.");
            return; // Exit the method to prevent further execution
        }

        String ratingStr = msg.substring(0, index);
        String feedback = msg.substring(index + 1);

        // Split the ratingStr by "/" and parse the first part as a float
        String[] parts = ratingStr.split("/");
        float rating = 0;
        if (parts.length > 0) {
            try {
                rating = Float.parseFloat(parts[0]);
            } catch (NumberFormatException e) {
                Log.e("ReviewActivity", "Failed to parse rating from string: " + parts[0], e);
                return; // Exit the method if parsing fails
            }
        }

        ratings.add(rating);
        float sumOfRatings = 0;
        for (Float r : ratings) {
            sumOfRatings += r;
        }

        float average = sumOfRatings / ratings.size();

        reviews.add(rating + "/5.0 " + feedback); // Ensure there's a space after "/5"
        // Update adapter with formatted string
        String reviewsString = String.join("\n", reviews); // Join reviews with newline breaks
        arrayAdapter.clear(); // Clear existing reviews before adding new ones
        arrayAdapter.addAll(reviewsString);
        arrayAdapter.notifyDataSetChanged();

        updateStarsImage(average);

        String avg = String.format("%.2f", average);
        Log.d("Average", avg);

        numStarsAvg.setText(avg + " / 5.0"); // Display average with "/ 5" to indicate out of 5
    }


    /**
     * Updates the stars image based on the average rating.
     *
     * @param average The average rating to determine the stars image.
     */
    private void updateStarsImage(float average) {
        if (average >= 0 && average < 0.5) {
            stars.setImageResource(R.drawable.zerostar);
        } else if (average >= 0.5 && average < 1) {
            stars.setImageResource(R.drawable.half);
        } else if (average >= 1 && average < 1.5) {
            stars.setImageResource(R.drawable.one);
        } else if (average >= 1.5 && average < 2) {
            stars.setImageResource(R.drawable.onehalf);
        } else if (average >= 2 && average < 2.5) {
            stars.setImageResource(R.drawable.two);
        } else if (average >= 2.5 && average < 3) {
            stars.setImageResource(R.drawable.twohalf);
        } else if (average >= 3 && average < 3.5) {
            stars.setImageResource(R.drawable.three);
        } else if (average >= 3.5 && average < 4) {
            stars.setImageResource(R.drawable.threehalfstars);
        } else if (average >= 4 && average < 4.5) {
            stars.setImageResource(R.drawable.four);
        } else if (average >= 4.5 && average < 5) {
            stars.setImageResource(R.drawable.fourhalf);
        } else {
            stars.setImageResource(R.drawable.five);
        }
    }

    /**
     * Callback method invoked when the WebSocket connection is closed.
     *
     * @param code   The status code of the close.
     * @param reason The reason for the close.
     * @param remote Indicates whether the close was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {}

    /**
     * Callback method invoked when an error occurs on the WebSocket connection.
     *
     * @param ex The exception representing the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {}

    /**
     * This method is called when the activity is being destroyed. It's overridden to
     * ensure proper cleanup of resources related to the WebSocket connection.
     * It disconnects the WebSocket and removes the WebSocket listener.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wsm.disconnectWebSocket();
        wsm.removeWebSocketListener();
    }
}