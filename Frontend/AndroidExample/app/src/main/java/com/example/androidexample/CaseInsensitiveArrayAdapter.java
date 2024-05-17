package com.example.androidexample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to provide adapter for searchable list view
 */
public class CaseInsensitiveArrayAdapter extends ArrayAdapter<Object> {
    private Activity context;
    private List<User> originalItems;
    private List<User> filteredItems;
    private int resource = R.layout.custom_listview_item;
    private OnItemClickListener onItemClickListener;


    public CaseInsensitiveArrayAdapter(Activity context, int resource, List<? extends User> items) {
        super(context, resource, Collections.singletonList(items));
        originalItems = new ArrayList<>(items);
        filteredItems = new ArrayList<>(items);

        this.context = context;

        if (items.size() == 0){
            Log.d("CaseInsensitiveArrayAdapter", "items: none");
        } else {
            Log.d("CaseInsensitiveArrayAdapter", "items: " + items.get(0).name);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(resource, null,true);

        TextView titleText = rowView.findViewById(R.id.title);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);

        titleText.setText("");
        if (filteredItems.get(position).name != null){
            if (!filteredItems.get(position).name.equals("null")){
                titleText.setText(filteredItems.get(position).name);
            }
        }
        if (filteredItems.get(position).getClass() == Band.class){
            Band band = (Band) filteredItems.get(position);
            LoadImage loader = new LoadImage(imageView);
            loader.execute(band.image);
        }

        if (filteredItems.get(position).getClass() == Venue.class){
            Venue venue = (Venue) filteredItems.get(position);
            LoadImage loader = new LoadImage(imageView);
            loader.execute(venue.image);
        }

        subtitleText.setText("");
        if (filteredItems.get(position).getClass() == Band.class){
            Band band = (Band) filteredItems.get(position);
            subtitleText.setText(band.genre);
        }

        if (filteredItems.get(position).getClass() == Venue.class){
            Venue venue = (Venue) filteredItems.get(position);
            subtitleText.setText(venue.genre);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });

        return rowView;

    };

    @NonNull
    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<User> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalItems);
                } else {
                    String filterPattern = constraint.toString();
                    for (User item : originalItems) {
                        if (Helper.containsIgnoreCase(item.name, filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems.clear();
                filteredItems.addAll((List<User>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public User getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Set the listener for each item in list
     * @param onItemClickListener : listener to set
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int id);
    }
}
