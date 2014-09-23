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
            ModelDatabase.CHAT_NAME,
            ModelDatabase.CHAT_MESSAGE,
            ModelDatabase.CHAT_TIMESTAMP,
    };

    // Main Activity is the context of the database
    public HandlerDatabase(Context context){
        model = new ModelDatabase(context);
    }

    /**
     * Add
     */
    public void addChatToDatabase(ChatModel chat){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.CHAT_NAME, chat.name);
        values.put(ModelDatabase.CHAT_MESSAGE, chat.message);
        values.put(ModelDatabase.CHAT_TIMESTAMP, chat.timestamp);
        database.insertWithOnConflict(ModelDatabase.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Delete
     */
    public void deleteChatByTimestamp(long timestamp){
        database.delete(
                ModelDatabase.TABLE_NAME,
                ModelDatabase.CHAT_TIMESTAMP + " like '%" + Long.toString(timestamp) + "%'",
                null
        );
    }

    /**
     * Get
     */
    public ArrayList<ChatModel> getAllChats(){
        return sweepCursor(database.query(ModelDatabase.TABLE_NAME, allColumns, null, null, null, null, null));
    }

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
                    Long.parseLong(cursor.getString(2))
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