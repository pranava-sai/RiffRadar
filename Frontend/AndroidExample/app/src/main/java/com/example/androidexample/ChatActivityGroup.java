// ReviewActivity.java

package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * ReviewActivity represents an activity in the Android application that displays reviews and ratings.
 * It establishes a WebSocket connection to receive real-time updates of reviews and ratings from a server.
 * Users can view the average rating, individual ratings with feedback, and corresponding star images.
 */
public class ChatActivityGroup extends AppCompatActivity implements WebSocketListener {
    private String name;
    /**
     * Represents an ImageView to display stars.
     */
    /**
     * Manages WebSocket connections for the application.
     */
    private WebSocketManager wsm;
    /**
     * Represents a ListView to display a list of reviews and ratings.
     */
    ListView list;
    /**
     * Stores a list of reviews and ratings, that will be displayed on the screen.
     */
    ArrayList<String> msgs;
    /**
     * Manages the data of the ListView by adapting an ArrayList of strings.
     */
    ArrayAdapter<String> arrayAdapter;

    private TextView groupName;
    private TextView messageSent;
    private EditText msg;
    private Button send;
    private int groupId;
    private String nameOfgroup;
    private ImageButton info;

    /**
     * This is the lifecycle method, that initializes variables and sets the screen for the user to view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        groupName = findViewById(R.id.groupName);
        send = findViewById(R.id.sendMsg);
        msg = findViewById(R.id.enterMessage);
        nameOfgroup = getIntent().getStringExtra("Name");
        groupName.setText(nameOfgroup);
        info = findViewById(R.id.info);
        groupId = getIntent().getIntExtra("ID",-1);
        list = findViewById(R.id.list);
        msgs = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgs);
        list.setAdapter(arrayAdapter);
        name = null;

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivityGroup.this, GroupInfoActivity.class);
                intent.putExtra("Name",nameOfgroup);
                intent.putExtra("ID",groupId);
                startActivity(intent);
            }
        });

        send.setOnClickListener(v -> {
            try {
                // Send message
                WebSocketManager.getInstance().sendMessage("id:"+groupId+"\r\n" + msg.getText().toString());

            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
            String s = msg.getText().toString();
        });

        // Prepare WebSocket link with username. Use of array, to split the user's name if in the form of First & Last
        String user = "";
        if (UserInfo.getInstance().loggedInUser != null) {
            user = UserInfo.getInstance().loggedInUser.name;
        } else {
            Log.e("ERROR",user);
        }

        String[] nameSplit = user.split("");
        if(nameSplit.length == 2) {
            name = nameSplit[1];
        } else {
            name = nameSplit[0];
        }
        // Websocket Link
        String wsLink = "ws://10.0.2.2:8080/groupchat/" + groupId +"/username"+"/"+name;
        //String wsLink = "ws://coms-309-017.class.las.iastate.edu:8080/groupchat/" + groupId +"/username"+"/"+name;

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
        msgs.add(message); // Ensure there's a space after "/5"
        // Update adapter with formatted string
        String msgString = String.join("\n", msgs); // Join reviews with newline breaks
        arrayAdapter.clear(); // Clear existing reviews before adding new ones
        arrayAdapter.addAll(msgString);
        arrayAdapter.notifyDataSetChanged();
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