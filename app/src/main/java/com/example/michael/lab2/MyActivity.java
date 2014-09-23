package com.example.michael.lab2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    ChatAdapter chatAdapter;
    public static String username = "default";
    public static String userId = "0001";

    HandlerDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my); // inflater (wat?)

        database = new HandlerDatabase(this);
        database.open();

        if (username.equals("default")){
            Toast.makeText(this, "You are signed in as default! Click SET USERNAME to change your name!", Toast.LENGTH_SHORT).show();
        }

        // makes sure chatAdapter exists, populate it with list of chats
        getChats();
        // sets chatList's adapter to chatAdapter, and binds click listener
        setupViews();
        Log.i(MyActivity.class.getSimpleName(), "Activity Initialized.");
    }

    private void getChats(){
        // make list of chats of type ChatModel
        List<ChatModel> newChats = database.getAllChats();
//        List<ChatModel> newChats = new ArrayList<ChatModel>();
        if (chatAdapter == null)
            chatAdapter = new ChatAdapter(this, R.layout.chat_item, new ArrayList<ChatModel>(), database);
        chatAdapter.addChats(newChats);
    }

    private void setupViews(){
        ListView chatList = (ListView) findViewById(R.id.main_output_layout);
        chatList.setAdapter(chatAdapter);
        chatList.setOnItemClickListener(ClickListeners.clickChatListener(this, chatAdapter, database));

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