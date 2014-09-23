package com.example.michael.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class HandlerDatabase {
    //Database Model
    private ModelDatabase model;

    //Database
    private SQLiteDatabase database;

    //All Fields
    private String[] allColumns = {
            ModelDatabase.CHAT_SENDER,
            ModelDatabase.CHAT_BODY,
            ModelDatabase.CHAT_USERID,
            ModelDatabase.CHAT_TIME,
    };

    // Main Activity is the context of the database
    public HandlerDatabase(Context context){
        model = new ModelDatabase(context);
    }

    /**
     * Add
     */
    public void addChatToDatabase(String sender, String body, String userId, long time){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.CHAT_SENDER, sender);
        values.put(ModelDatabase.CHAT_BODY, body);
        values.put(ModelDatabase.CHAT_USERID, userId);
        values.put(ModelDatabase.CHAT_TIME, time);
        database.insertWithOnConflict(ModelDatabase.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
    public void addChatToDatabase(ChatModel chat){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.CHAT_SENDER, chat.sender);
        values.put(ModelDatabase.CHAT_BODY, chat.body);
        values.put(ModelDatabase.CHAT_USERID, chat.userId);
        values.put(ModelDatabase.CHAT_TIME, chat.time);
        database.insertWithOnConflict(ModelDatabase.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
    public void updateChat(ChatModel chat){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.CHAT_SENDER, chat.sender);
        values.put(ModelDatabase.CHAT_BODY, chat.body);
        values.put(ModelDatabase.CHAT_USERID, chat.userId);
        values.put(ModelDatabase.CHAT_TIME, chat.time);
        database.update(ModelDatabase.TABLE_NAME, values, ModelDatabase.CHAT_TIME + " like '%" + Long.toString(chat.time) + "%'", null);
    }

    /**
     * Delete
     */
    public void deleteChatByTime(long time){
        database.delete(
                ModelDatabase.TABLE_NAME,
                ModelDatabase.CHAT_TIME + " like '%" + Long.toString(time) + "%'",
                null
        );
    }
    public void deleteAllChats(){
        database.delete(
                ModelDatabase.TABLE_NAME,
                null,
                null
        );
    }

    /**
     * Get
     */
    public ArrayList<ChatModel> getAllChats(){
        return sweepCursor(database.query(ModelDatabase.TABLE_NAME, allColumns, null, null, null, null, null));
    }
    public ArrayList<ChatModel> getChatBySender(String sender){
        return sweepCursor(database.query(
                ModelDatabase.TABLE_NAME,
                allColumns,
                ModelDatabase.CHAT_SENDER + " like '%" + sender + "%'",
                null, null, null, null, null
        ));
    }
    public ChatModel getChatByTime(long time){
        return sweepCursor(database.query(
                ModelDatabase.TABLE_NAME,
                allColumns,
                ModelDatabase.CHAT_TIME + " like '%" + Long.toString(time) + "%'",
                null, null, null, null
        )).get(0);
    }

    /**
     * Additional Helpers
     */
    // Sweep Through Cursor and return a List of Chats
    private ArrayList<ChatModel> sweepCursor(Cursor cursor){
        ArrayList<ChatModel> chats = new ArrayList<ChatModel>();

        //Get to the beginning of the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            //Get the Chat
            ChatModel chat = new ChatModel(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Long.parseLong(cursor.getString(3), 10)
            );
            //Add the Chat
            chats.add(chat);
            //Go on to the next Chat
            cursor.moveToNext();
        }
        return chats;
    }

    //Get Writable Database - open the database
    public void open(){
        database = model.getWritableDatabase();
    }
}