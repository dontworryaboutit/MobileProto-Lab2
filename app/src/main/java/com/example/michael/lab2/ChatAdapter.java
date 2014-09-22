package com.example.michael.lab2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class ChatAdapter extends ArrayAdapter {
    private List<ChatModel> chats = new ArrayList<ChatModel>();
    private int resource;
    private Context context;
    private HandlerDatabase database;

    public ChatAdapter(Context context, int resource, List<ChatModel> chats, HandlerDatabase database) {
        super(context, resource);
        // make context accessible from outside adapter (k?)
        this.context = context;
        this.resource = resource;
        this.database = database;
        addChats(chats);
    }

    private class ChatHolder {
        // maintains view information
        TextView name, body, time;
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

            // holder keeps all found views from last time (we can update the views directly without re-finding them)
            chatHolder = new ChatHolder();

            // find elements in chat item, cast to views
            chatHolder.name = (TextView) convertView.findViewById(R.id.chat_item_name);
            chatHolder.body = (TextView) convertView.findViewById(R.id.chat_item_msg);
            chatHolder.time = (TextView) convertView.findViewById(R.id.chat_item_time);
            chatHolder.background = convertView.findViewById(R.id.chat_item_img);
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
        holder.name.setText(chat.sender);
        holder.body.setText(chat.body);
        holder.time.setText(formatTime(chat.time));
    }

    private String formatTime(long time) {
        if (DateUtils.isToday(time)) {
            return new SimpleDateFormat("E, hh:mm:ss a").format(new Date(time));
        }
        return new SimpleDateFormat("MM/DD, hh:mm:ss a").format(new Date(time));
    }

    private Drawable getProfileDrawable(String id) {
        return null;
    }

    public void addChats(List<ChatModel> newChats) {
        this.chats.addAll(newChats);
        notifyDataSetChanged();
    }

    public void addChat(ChatModel chat) {
        this.chats.add(chat);
        this.database.addChatToDatabase(chat);
        notifyDataSetChanged();
    }
}