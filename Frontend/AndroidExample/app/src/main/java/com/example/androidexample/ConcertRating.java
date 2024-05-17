package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Activity class representing the rating screen for a concert.
 * Allows users to rate and provide feedback for a concert.
 */
public class ConcertRating extends AppCompatActivity implements WebSocketListener{


    private Button submitBtn;
    private EditText addReview;
    private RatingBar rating;
    private int concert_id;

    /**
     * Initializes UI elements and sets up WebSocket connection.
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fivestarrating);

        /* initialize UI elements */
        rating = (RatingBar) findViewById(R.id.ratingBar);
        submitBtn = (Button) findViewById(R.id.submitRating);
        addReview = (EditText) findViewById(R.id.addFeedback);
        concert_id = getIntent().getIntExtra("ID",-1);

        /* send button listener */
        submitBtn.setOnClickListener(v -> {
            try {
                // Send message
                Log.d("MESSAGE DEBUG",addReview.getText().toString());
                String ratingPoints = String.valueOf(rating.getRating());
                WebSocketManager.getInstance().sendMessage("id:"+concert_id+"\r\n" +ratingPoints + "\r\n" + addReview.getText().toString());
                addReview.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
            String s = addReview.getText().toString();
        });
    }

    /**
     * Sets up WebSocket connection when activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        WebSocketManager.getInstance().setWebSocketListener(ConcertRating.this);
        String user = UserInfo.getInstance().loggedInUser.name;
        String[] nameSplit = user.split(" ");
        String name;
        if (nameSplit.length == 2) {
            name = nameSplit[1];
        } else {
            name = nameSplit[0];
        }
        String wsLink = "ws://coms-309-017.class.las.iastate.edu:8080/review/" + name + "/"+concert_id;
        WebSocketManager.getInstance().connectWebSocket(wsLink);
    }

    /**
     * Handles incoming WebSocket messages.
     * @param message received WebSocket message
     */
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */

//        runOnUiThread(() -> {
//
//
//
//        });
    }

    /**
     * Handles WebSocket closure.
     * @param code status code
     * @param reason reason for closure
     * @param remote indicates whether the closure was initiated remotely
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        Log.d("DEBUG","---\nconnection closed by " + closedBy + "\nreason: " + reason);
    }

    /**
     * Handles WebSocket opening.
     * @param handshakedata WebSocket handshake data
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    /**
     * Handles WebSocket errors.
     * @param ex exception representing the error
     */
    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("ERROR",ex.toString());
    }
}
