package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

/**
 * the screen to direct message for a user to direct message with another user.
 */
public class ChatActivity extends AppCompatActivity implements WebSocketListener{

    private ImageView backButton, deleteButton;
    private TextView nameText;

    private EditText enterMessage;
    private TextView sendButton;

    private RecyclerView chatsDisplay;
    private ChatAdapter chatAdapter;

    private String source;
    private String destination;

//    private final String webSocketBaseURL= "ws://10.0.2.2:8080/chat/"; // local
    private final String webSocketBaseURL= "ws://coms-309-017.class.las.iastate.edu:8080/chat/"; // server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* get intent */
        Intent intent = getIntent();
        source = intent.getExtras().getString("source", "null");
        destination = intent.getExtras().getString("dest", "null");

        /* connect to ws */
        connect(source, destination);

        /* initialize UI elements */
        initUI();

        /* connect this activity to the websocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* set listeners */
        setListeners();

    }

    @Override
    protected void onDestroy() {
        WebSocketManager.getInstance().disconnectWebSocket();

        super.onDestroy();
    }

    private void initUI(){
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        nameText = findViewById(R.id.nameText);
        nameText.setText(getIntent().getStringExtra("name"));

        enterMessage = findViewById(R.id.enterMessage);
        sendButton = findViewById(R.id.sendButton);

        chatsDisplay = findViewById(R.id.chats);
        chatAdapter = new ChatAdapter(getLayoutInflater());
        chatsDisplay.setAdapter(chatAdapter);
        chatsDisplay.setLayoutManager(new LinearLayoutManager(this));
    }

    private void connect(String source, String destination){
        String url = webSocketBaseURL + source + "/" + destination;

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(url);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }
    private void setListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){ //enter key code
                    sendChat();
                    //exit
                    return true;
                }
                return false;
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            //send message
            @Override
            public void onClick(View v) {
                sendChat();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = null;
                if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equalsIgnoreCase("band")){
                    URL = Helper.baseURL + "/venues/" + destination + "/bands/" + UserInfo.getInstance().loggedInUser.id;
                } else if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equalsIgnoreCase("venue")){
                    URL = Helper.baseURL + "/venues/" + UserInfo.getInstance().loggedInUser.id + "/bands/" + destination;
                }
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ChatActivity.this, "Chat deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UserProfile", "Error deleting user", error);
                        // You might want to inform the user about the error
                        Toast.makeText(ChatActivity.this, "Error deleting chat", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
            }
        });

    }

    private void sendChat() {
        String text = enterMessage.getText().toString();
        if (text != null){
            WebSocketManager.getInstance().sendMessage("@" + destination + " " + text);
            //clear
            enterMessage.setText("");
            //hide keyboard
            Helper.hideKeyboard(ChatActivity.this);
        }
    }

    /**
     * Used with WebSocket. No behavior but is required by the interface.
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
    }

    /**
     * When a message is received from the WebSocket, it is displayed to the user.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        Log.d("onWebSocketMessage()", message);

        runOnUiThread(() -> {

            try {
                if (message.startsWith("History")){
                    Log.d("message gathered","history");

                    String history = message.substring("History".length() + 1);

                    String[] messages = history.split("\n");
                    int length = messages.length;
                    for (int i = 0; i < length; i++){
                        JSONObject chat = new JSONObject();
                        if (messages[i].startsWith(source)){
                            chat.put("msg", messages[i].substring(destination.length() + 2));
                            chat.put("type", 1);
                        } else if (messages[i].startsWith(destination)){
                            chat.put("msg", messages[i].substring(destination.length() + 2));
                            chat.put("type", 2);
                        } else {
                            chat.put("msg", messages[i]);
                            chat.put("type", 0);
                        }

                        chatAdapter.addChat(chat);
                    }
                } else if (message.startsWith(source)){
                    Log.d("message gathered","sent");

                    JSONObject chat = new JSONObject();
                    String newMessage = message.substring(source.length() + 2);
                    chat.put("msg", newMessage);
                    chat.put("type", 1);

                    Log.d("sent", chat.toString());
                    chatAdapter.addChat(chat);
                } else if (message.startsWith(destination)){
                    Log.d("message gathered","collected");

                    JSONObject chat = new JSONObject();
                    String newMessage = message.substring(destination.length() + 2);
                    chat.put("msg", newMessage);
                    chat.put("type", 2);

                    Log.d("collected", chat.toString());
                    chatAdapter.addChat(chat);
                } else {
                    Log.d("message gathered","system");

                    //display as system
                    JSONObject chat = new JSONObject();
                    chat.put("msg", message);
                    chat.put("type", 0);

                    Log.d("system", chat.toString());
                    chatAdapter.addChat(chat);
                }

                chatsDisplay.scrollToPosition(chatAdapter.getItemCount() - 1);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * When the web socket is closed, prints a message explaining.
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            try {
                JSONObject chat = new JSONObject();
                chat.put("msg", "connection closed by " + closedBy + "\nreason: " + reason);
                chat.put("type", 0);
                chatAdapter.addChat(chat);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Used with WebSocket. No behavior but is required by the interface.
     * @param ex The error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        ex.printStackTrace();
    }
}