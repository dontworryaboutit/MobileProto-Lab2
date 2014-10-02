package com.example.michael.lab2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    ChatAdapter chatAdapter;
    public static String username = "Searing";

    HandlerDatabase database;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my); // inflater (wat?)

        // connect app to its associated data storage
        setupDatabases();
        // makes sure chatAdapter exists, populate it with list of chats
        getChats();
        // sets chatList's adapter to chatAdapter, and binds click listener
        setupViews();
        Log.i(MyActivity.class.getSimpleName(), "Activity Initialized.");
    }

    private void setupDatabases(){
        database = new HandlerDatabase(this);
        database.open();
//        firebase = new Firebase("https://mobileproto2014.firebaseio.com/chatroom/0");
        firebase = new Firebase("https://fiery-heat-9884.firebaseio.com/chatroom/0");
        firebase.addChildEventListener(ClickListeners.firebaseChildListener(this, chatAdapter));
    }

    private void getChats(){
        // make list of chats of type ChatModel
        List<ChatModel> newChats = database.getAllChats();
//        List<ChatModel> newChats = new ArrayList<ChatModel>();
        if (chatAdapter == null)
            chatAdapter = new ChatAdapter(this, R.layout.chat_item, database, firebase, new ArrayList<ChatModel>());
        chatAdapter.populateChats(newChats);
    }

    private void setupViews(){
        ListView chatList = (ListView) findViewById(R.id.main_output_layout);
        chatList.setAdapter(chatAdapter);
        chatList.setOnItemClickListener(ClickListeners.clickChatListener(this, chatAdapter));

        final EditText input = (EditText) findViewById(R.id.main_input_entry);
        input.clearFocus();

        Button sendButton = (Button) findViewById(R.id.main_input_button);
        sendButton.setOnClickListener(ClickListeners.sendButtonListener(this, chatAdapter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.menu_change_username:
                ClickListeners.changeUsernameListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}