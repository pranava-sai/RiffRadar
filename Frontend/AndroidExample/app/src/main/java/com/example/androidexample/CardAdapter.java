package com.example.androidexample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Connects the recycler in chat to the correct UI element.
 */
public class CardAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<JSONObject> cards = new ArrayList<JSONObject>();
    private OnClickListener onClickListener;
    private Context context;

    /**
     * Constructor for chat adapter.
     *
     * @param inflater : A new view relating to a declared XML element.
     */
    public CardAdapter(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
    }


    private class CardContainer extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView price;
        String url;
        int id;

        public CardContainer(@NonNull View item) {
            super(item);
            setIsRecyclable(false);
            img = item.findViewById(R.id.image);
            name = item.findViewById(R.id.name);
            price = item.findViewById(R.id.price);
        }
    }

    /**
     * Creates a view connected to the correct XML element.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return the desired view
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.card, parent, false);
        return new CardContainer(view);
    }

    /**
     * Based on the type, connects to the correct XML.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject card = cards.get(position);
        CardContainer cardContainer = (CardContainer) holder;

        cardContainer.name.setText("");
        try {
            if (!card.getString("name").equals("null")){
                cardContainer.name.setText(card.getString("name"));
            }
        } catch (Exception e) {
        }

        cardContainer.id = 0;
        try {
            cardContainer.id = card.getInt("id");
        } catch (Exception e) {
        }

        cardContainer.price.setText("");
        try {
            if (!card.getString("price").equals("null")){
                cardContainer.price.setText(card.getString("price"));
            }
        } catch (Exception e) {
        }

        cardContainer.url = "";
        try {
            cardContainer.url = card.getString("url");
        } catch (Exception e) {
        }

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.blank);
        cardContainer.img.setImageDrawable(drawable);
        try {
            String imgURL = card.getString("image");
            LoadImage loader = new LoadImage(cardContainer.img);
            loader.execute(imgURL);
        } catch (Exception e) {
            cardContainer.img.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.blank));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(cardContainer.id, cardContainer.url);
                }
            }
        });
    }

    /**
     * Getter method for number of elements
     *
     * @return number of elements
     */
    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * Adds a chat to the current chats and notifies that this change occured.
     *
     * @param card : the chat to add
     */
    public void addCard(JSONObject card) {
        cards.add(card);
        notifyDataSetChanged();
    }

    /**
     * removes all cards
     */
    public void clearCards() {
        cards.clear();
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int id, String URL);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
