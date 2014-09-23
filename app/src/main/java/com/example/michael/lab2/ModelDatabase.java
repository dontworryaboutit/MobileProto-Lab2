package com.example.michael.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ModelDatabase extends SQLiteOpenHelper {
    //Table Name
    public static final String TABLE_NAME = "ChatMessages";

    //Table Fields
    public static final String CHAT_NAME = "name";
    public static final String CHAT_MESSAGE = "message";
    public static final String CHAT_TIMESTAMP = "timestamp";

    //Database Info
    private static final String DATABASE_NAME = "ChatAppDatabase";
    private static final int DATABASE_VERSION = 1;

    // ModelDatabase creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + CHAT_NAME + " TEXT NOT NULL, "
            + CHAT_MESSAGE + " TEXT NOT NULL, "
            + CHAT_TIMESTAMP + " TEXT NOT NULL UNIQUE );";

    // Default SQLite Constructor
    public ModelDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //OnCreate Method - creates the ModelDatabase
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }
    @Override
    //OnUpgrade Method - upgrades ModelDatabase if applicable
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(ModelDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}