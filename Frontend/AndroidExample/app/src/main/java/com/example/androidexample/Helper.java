package com.example.androidexample;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Class containing helper info for other classes
 */
public class Helper {

    /**
     * Base url for whole app to use
     */
    public static final String baseURL = "http://coms-309-017.class.las.iastate.edu:8080";

    /**
     * Finds number of items in a list view and sets height
     * @param listView : element containing items to count
     */
    public static void setListViewSize(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        // max of 5 elements
        int max = 5;

        for (int size = 0; ((size < listAdapter.getCount()) && (size < max)); size++) {
            View listItem = listAdapter.getView(size, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            Log.d("addHeight", "height: " + totalHeight);
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = (int) ((totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))) * .2);
        listView.setLayoutParams(params);

        Log.d("setListViewSize()", "height: " + totalHeight);
    }


    /**
     * Hide keyboard.
     * @param activity : Current activity.
     */
    public static void hideKeyboard(Activity activity) {
        //get input method
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //get the current focus
        View view = activity.getCurrentFocus();

        //create a new window if there is none
        if (view == null) {
            view = new View(activity);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    /**
     * faster case insensitive contains
     *
     * @param src : the string to be searched
     * @param what : the pattern
     * @return
     */
    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}