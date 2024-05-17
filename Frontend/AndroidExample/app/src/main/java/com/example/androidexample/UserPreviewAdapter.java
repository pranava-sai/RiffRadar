package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Connects the recycler used to preview users to the correct UI element.
 */
public class UserPreviewAdapter extends RecyclerView.Adapter {
    private OnClickListener onClickListener;
    private LayoutInflater inflater;
    private ArrayList<User> users = new ArrayList<>();

    /**
     * Constructor for user adapter.
     * @param inflater : A new view relating to a declared XML element.
     */
    public UserPreviewAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    private class PreviewContainer extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView icon;
        public PreviewContainer(@NonNull View item){
            super(item);
            title = item.findViewById(R.id.title);
            subtitle = item.findViewById(R.id.subtitle);
            icon = item.findViewById(R.id.icon);
        }
    }

    /**
     *
     * Creates a view connected to the XML element.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the desired view
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.chat_preview, parent, false);

        return new PreviewContainer(view);
    }

    /**
     * Connects to the XML.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = users.get(position);

        PreviewContainer previewContainer = (PreviewContainer) holder;

        previewContainer.title.setText("");
        if (user.name != null){
            if (!user.name.equals("null")){
                previewContainer.title.setText(user.name);
            }
        }
        if (user.getClass() == Band.class){
            Band band = (Band) user;
            LoadImage loader = new LoadImage(previewContainer.icon);
            loader.execute(band.image);
        }

        if (user.getClass() == Venue.class){
            Venue venue = (Venue) user;
            LoadImage loader = new LoadImage(previewContainer.icon);
            loader.execute(venue.image);
        }

        previewContainer.subtitle.setText("");
        if (user.getClass() == Band.class){
            Band band = (Band) user;
            previewContainer.subtitle.setText(band.genre);
        }

        if (user.getClass() == Venue.class){
            Venue venue = (Venue) user;
            previewContainer.subtitle.setText(venue.genre);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(user);
                }
            }
        });
    }

    /** sets the onClickListener for the user preview */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /** the methods allowed by the listener */
    public interface OnClickListener {
        void onClick(User user);
    }

    /**
     * Getter method for number of elements
     * @return number of elements
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Adds a user to the current users and notifies that this change occurred.
     * @param user : the user to add
     */
    public void addUser (User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    /**
     * reset the users to 0
     */
    public void clearUsers () {
        users.clear();
        notifyDataSetChanged();
    }
}
