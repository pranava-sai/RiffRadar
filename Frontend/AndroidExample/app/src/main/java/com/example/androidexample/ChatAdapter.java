package com.example.androidexample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Connects the recycler in chat to the correct UI element.
 */
public class ChatAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<JSONObject> chats = new ArrayList<JSONObject>();

    /** Label to show the label has not been set. */
    public static final int DEFAULT_LABEL = -1;

    /** Label to show the message came from neither user in the chat. */
    public static final int SYSTEM_LABEL = 0;

    /** Label to show the chat was sent by the current user. */
    public static final int SENT_LABEL = 1;

    /** Label to show a chat was sent to the current user by another user. */
    public static final int COLLECTED_LABEL = 2;

    /**
     * Constructor for chat adapter.
     * @param inflater : A new view relating to a declared XML element.
     */
    public ChatAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }


    private class SystemMessageContainer extends RecyclerView.ViewHolder {
        TextView message;
        public SystemMessageContainer(@NonNull View item){
            super(item);
            message = item.findViewById(R.id.systemChat);
        }
    }
    private class SentMessageContainer extends RecyclerView.ViewHolder {
        TextView message;
        public SentMessageContainer(@NonNull View item){
            super(item);
            message = item.findViewById(R.id.sentChat);
        }
    }
    private class CollectedMessageContainer extends RecyclerView.ViewHolder {
        TextView message;
        public CollectedMessageContainer(@NonNull View item){
            super(item);
            message = item.findViewById(R.id.collectedChat);
        }
    }

    /**
     * Allows classes other than the adapter to see if the message was system, sent, or collected.
     * @param index position in adapter
     * @return label (int) corresponding to the desired chat's type
     */
    @Override
    public int getItemViewType(int index){
        JSONObject chat = chats.get(index);

        try {
            int type = chat.getInt("type");
            switch (type){
                case SYSTEM_LABEL:
                    return SYSTEM_LABEL;
                case SENT_LABEL:
                    return SENT_LABEL;
                case COLLECTED_LABEL:
                    return COLLECTED_LABEL;
                default:
                    return DEFAULT_LABEL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return DEFAULT_LABEL;
    }

    /**
     *
     * Creates a view connected to the correct XML element.
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

        switch (viewType){
            case SYSTEM_LABEL:
                view = inflater.inflate(R.layout.chat_system, parent, false);
                return new SystemMessageContainer(view);
            case SENT_LABEL:
                view = inflater.inflate(R.layout.chat_sent, parent, false);
                return new SentMessageContainer(view);
            case COLLECTED_LABEL:
                view = inflater.inflate(R.layout.chat_collected, parent, false);
                return new CollectedMessageContainer(view);
            default:
                return null;
        }
    }

    /**
     * Based on the type, connects to the correct XML.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject chat = chats.get(position);

        try {
            int type = chat.getInt("type");
            switch (type){
                case SENT_LABEL:
                    SentMessageContainer sentContainer = (SentMessageContainer) holder;
                    sentContainer.message.setText(chat.getString("msg"));
                    break;
                case COLLECTED_LABEL:
                    CollectedMessageContainer collectedContainer = (CollectedMessageContainer) holder;
                    collectedContainer.message.setText(chat.getString("msg"));
                    break;
                default:
                    SystemMessageContainer systemContainer = (SystemMessageContainer) holder;
                    systemContainer.message.setText(chat.getString("msg"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method for number of elements
     * @return number of elements
     */
    @Override
    public int getItemCount() {
        return chats.size();
    }

    /**
     * Adds a chat to the current chats and notifies that this change occured.
     * @param chat : the chat to add
     */
    public void addChat (JSONObject chat) {
        Log.d("addChat", "chat: " + chat.toString());
        chats.add(chat);
        notifyDataSetChanged();
    }
}
