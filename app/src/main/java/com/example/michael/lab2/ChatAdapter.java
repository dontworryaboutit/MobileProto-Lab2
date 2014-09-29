package com.example.michael.lab2;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends ArrayAdapter {
    private List<ChatModel> chats = new ArrayList<ChatModel>();
    private int resource;
    private Context context;
    private HandlerDatabase database;
    private Firebase firebase;

    public ChatAdapter(Context context, int resource, HandlerDatabase database, Firebase firebase, List<ChatModel> chats) {
        super(context, resource);
        // make context accessible from outside adapter (k?)
        this.context = context;
        this.resource = resource;
        this.database = database;
        this.firebase = firebase;
        populateChats(chats);
    }

    private class ChatHolder {
        // maintains view information
        TextView name, message, timestamp;
        ImageView picture;
        View background;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView is child
        ChatHolder chatHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) { // this child is new // has not been rendered
            // inflater takes id of chat item and parent view
            convertView = inflater.inflate(resource, parent, false);

            // holder keeps all found views from last timestamp (we can update the views directly without re-finding them)
            chatHolder = new ChatHolder();

            // find elements in chat item, cast to views
            chatHolder.name = (TextView) convertView.findViewById(R.id.chat_item_name);
            chatHolder.message = (TextView) convertView.findViewById(R.id.chat_item_msg);
            chatHolder.timestamp = (TextView) convertView.findViewById(R.id.chat_item_time);
            chatHolder.picture = (ImageView) convertView.findViewById(R.id.chat_item_pic);
            convertView.setTag(chatHolder);
        } else {
            chatHolder = (ChatHolder) convertView.getTag();
        }

        fillViews(chatHolder, chats.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return this.chats.size();
    }

    private void fillViews(ChatHolder holder, ChatModel chat) {
        holder.name.setText(chat.name);
        holder.message.setText(chat.message);
        holder.timestamp.setText(formatTime(chat.timestamp));
    }

    private String formatTime(long timestamp) {
        if (DateUtils.isToday(timestamp)) {
            return new SimpleDateFormat("E, hh:mm:ss a").format(new Date(timestamp));
        }
        return new SimpleDateFormat("MM/DD, hh:mm:ss a").format(new Date(timestamp));
    }

    public ChatModel getChat(int index) {
        if(index + 1 > this.chats.size() || index < 0) {
            return null;
        } else {
            return this.chats.get(index);
        }
    }

    public String getChatMessage(int index) {
        return this.chats.get(index).message;
    }

    public void deleteChat(ChatModel chat) {
        this.chats.remove(chat);
        this.database.deleteChatByTimestamp(chat.timestamp);
        notifyDataSetChanged();
    }

    public void updateChatMessage(int index, String newMessage) {
        ChatModel chat = getChat(index);
        chat.message = newMessage;
        this.database.updateChatByTimestamp(chat, chat.timestamp);
        notifyDataSetChanged();
    }

    public void populateChats(List<ChatModel> newChats) {
//      used to pull chats from sql database on phone
//      only called upon app restart (in initialization of activity)
        this.chats.addAll(newChats);
        notifyDataSetChanged();
    }

    public void addChat(ChatModel chat) {
//      adds a new chat to current list, and to database
//      only called once in each chat's lifetime
        this.chats.add(chat);
        this.database.addChatToDatabase(chat);
        addChatToFirebase(chat);
        notifyDataSetChanged();
    }

    public void addChatToFirebase(ChatModel chat) {
//      creates new object in firebase
//      note that ref.setValue() overwrites all data at ref
//        Firebase usersRef = this.firebase.child("users");
//        usersRef.setValue(chat);
//        this.firebase.push();
//        ref.setValue(chat);
        ChatModel testObject = new ChatModel("Searing", "Hello, World!");
        Firebase ref = firebase.child(Long.toString(testObject.timestamp));
        ref.setValue(testObject);
    }
}