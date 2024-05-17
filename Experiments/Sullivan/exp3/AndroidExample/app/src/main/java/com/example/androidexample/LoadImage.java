package com.example.androidexample;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/* class that loads image from web. this task must be done off of the main thread */
public class LoadImage extends AsyncTask<String, Void, Drawable> {
    private ImageView imageView; //imageView to be set

    /* class must be constructed with an imageView so the imageView can be set in post */
    LoadImage(ImageView imageView){
        this.imageView = imageView;
    }

    /* the task to complete off of the main thread, try and catch any exceptions */
    @Override
    protected Drawable doInBackground(String... params) {
        String url = params[0];
        try {
            InputStream stream = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(stream, "src");
        } catch (Exception e) {
            return null;
        }
    }

    /* set image on post if valid */
    @Override
    protected void onPostExecute(Drawable result) {
        if (result != null) {
            imageView.setImageDrawable(result);
        }
    }
}
