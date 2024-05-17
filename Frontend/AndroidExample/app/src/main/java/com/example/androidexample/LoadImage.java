package com.example.androidexample;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.URL;

/** class that loads image from web. this task must be done off of the main thread */
public class LoadImage extends AsyncTask<String, Void, Drawable> {
    private ImageView imageView; //imageView to be set

    /** class must be constructed with an imageView so the imageView can be set after */
    LoadImage(ImageView imageView){
        this.imageView = imageView;
    }

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

    @Override
    protected void onPostExecute(Drawable result) {
        if (result != null) {
            imageView.setImageDrawable(result);
        }
    }
}
