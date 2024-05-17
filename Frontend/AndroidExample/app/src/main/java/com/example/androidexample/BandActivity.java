package com.example.androidexample;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Shows information relevant to the selected band.
 */
public class BandActivity extends AbstractElementActivity {

    private ImageView spotifyImg;
    private TextView spotifySong;
    private TextView spotifyBand;

    private ImageView playButton;
    private ImageView pauseButton;
    private ProgressBar progressBar;
    private CountDownTimer timer = null;
    private int time = 0;

    private MediaPlayer mediaPlayer;

    private String audioUrl;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        childURL = Helper.baseURL + "/bands";
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (UserInfo.hasInstance()){
            if (UserInfo.getInstance().loggedInUser.loginInfo.userType.equals("venue")){
                chatButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        getToken();
    }

    @Override
    protected void onDestroy(){
        if (pauseButton.getVisibility() == View.VISIBLE){
            stopSong();
        }

        super.onDestroy();
    }

    @Override
    protected void initUI(){
        super.initUI();

        spotifyImg = findViewById(R.id.spotifyImg);
        spotifySong = findViewById(R.id.spotifySongName);
        spotifyBand = findViewById(R.id.spotifyBandName);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void setUIListeners(){
        super.setUIListeners();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    pauseButton.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.GONE);

                    timer = new CountDownTimer(30000,500) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            time++;
                            progressBar.setProgress(time * 6);

                        }

                        @Override
                        public void onFinish() {
                            if (mediaPlayer != null){
                                stopSong();
                            }
                            time = 0;
                            progressBar.setProgress(0);
                            pauseButton.setVisibility(View.GONE);
                            playButton.setVisibility(View.VISIBLE);
                        }
                    };
                    timer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSong();

                if (timer != null){
                    timer.cancel();
                    time = 0;
                    progressBar.setProgress(0);
                }

                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void setDetails(JSONObject response) {
        super.setDetails(response);

        setOneDetail(response, "name", spotifyBand);

        try {
            if (response.has("address")) {
                JSONObject address = response.getJSONObject("address");
                String state = address.getString("state");
                if (!state.equals("null")) {
                    String city = address.getString("city");
                    if (!city.equals("null")) {
                        map.onCreate(null);
                        map.getMapAsync(this);
                        addressView.setText(city + ", " + state);
                        setPos(city, state);
                    } else {
                        map.onCreate(null);
                        map.getMapAsync(this);
                        addressView.setText(state);
                        setPos(state);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Error", "Set Details");
        }
    }

    private void getToken() {
        String clientId = "14c61886e8c54d7f97767b7408110b8d";
        String clientSecret = "0232e5cfeb1f4179bc4a208e3e0f4fcc";
        String tokenUrl = "https://accounts.spotify.com/api/token";

        String clientCredentials = clientId + ":" + clientSecret;
        String clientCredentialsBase64 = android.util.Base64.encodeToString(clientCredentials.getBytes(), android.util.Base64.NO_WRAP);

        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.POST, tokenUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("access_token")){
                                accessToken = response.getString("access_token");
                                getArtist(nameView.getText().toString());
                                Log.d("token", response.toString());
                            }

                        } catch (JSONException e) {
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
                headers.put("Authorization", "Basic " + clientCredentialsBase64);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            public byte[] getBody() {
                return "grant_type=client_credentials".getBytes();
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void getArtist(String artist){
        String url = "https://api.spotify.com/v1/search?q=" + artist + "&type=artist";
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", "response: " + response.toString());
                try {
                    if (response.has("artists")){
                        JSONObject artists = response.getJSONObject("artists");
                        if (artists.has("items")){
                            JSONObject band = artists.getJSONArray("items").getJSONObject(0);
                            if (band.has("id")){
                                getSong(band.getString("id"));
                            }
                        }
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
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        Log.d("GET", "artist request: " + objReq.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void getSong(String id){
        Log.d("band id", id);
        String url = "https://api.spotify.com/v1/artists/" + id + "/top-tracks";
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Volley Response", "response: " + response.toString());
                try {
                    if (response.has("tracks")) {
                        JSONObject track = response.getJSONArray("tracks").getJSONObject(0);

                        spotifyContainer.setVisibility(View.VISIBLE);

                        if (track.has("album")){
                            JSONObject album = track.getJSONObject("album");
                            if (album.has("images")){
                                JSONObject image = album.getJSONArray("images").getJSONObject(0);
                                if (image.has("url")){
                                    String spotifyImgURL = image.getString("url");
                                    LoadImage loader = new LoadImage(spotifyImg);
                                    loader.execute(spotifyImgURL);
                                    playButton.getLayoutParams().height = spotifyImg.getLayoutParams().height;
                                    pauseButton.getLayoutParams().height = spotifyImg.getLayoutParams().height;
                                    playButton.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        if (track.has("name")){
                            spotifySong.setText(track.getString("name"));
                        }

                        if (track.has("preview_url")){
                            audioUrl = track.getString("preview_url");
                        }
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
                headers.put("Authorization", "Bearer " + accessToken);
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
        Log.d("GET", "artist request: " + objReq.toString());

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(objReq);
    }

    private void stopSong(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

}

