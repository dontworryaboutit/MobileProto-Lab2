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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyActivity extends Activity {
    ChatAdapter chatAdapter;
    HandlerDatabase database;
    Firebase firebase;
    public static String username = "Searing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my); // inflater (wat?)

        setupDatabase(); // connect app to its SQLite database
        getChats(); // makes sure chatAdapter exists, populate it with list of chats
        setupFirebase(); // connect app to its Firebase database
        setupViews(); // sets chatList's adapter to chatAdapter, and binds click listener
    }

    public void getString() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i(MyActivity.class.getSimpleName(), response.substring(0,500));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyActivity.class.getSimpleName(), "nope.");
                    }
                }
        );
    // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getJson() {
        final String URL = "/volley/resource/12";
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }
        );

//      add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public void postJson() {
        final String URL = "http://api.indico.io/political";
//      Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("text", "I'm good");

        JsonObjectRequest req = new JsonObjectRequest(
                URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.i(MyActivity.class.getSimpleName(), response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }
        );

//      add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    public void getJsonArray() {
        final String URL = "/volley/resource/all?count=20";
        JsonArrayRequest req = new JsonArrayRequest(
                URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }
        );

//      add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private void setupDatabase(){
//        firebase = new Firebase("https://mobileproto2014.firebaseio.com/chatroom/0");
        firebase = new Firebase("https://fiery-heat-9884.firebaseio.com/chatroom/0");

        database = new HandlerDatabase(this);
        database.open();
    }

    private void setupFirebase(){
        Query postsQuery = firebase.limit(10);
        postsQuery.addChildEventListener(ClickListeners.firebaseChildListener(this, chatAdapter));
//        firebase.addChildEventListener(ClickListeners.firebaseChildListener(this, chatAdapter));
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
            case R.id.menu_push_chats:
                ClickListeners.pushChatsListener(this, chatAdapter);
                return true;
            case R.id.menu_delete_chats:
                ClickListeners.deleteChatsListener(this, chatAdapter);
                return true;
            case R.id.derp:
                postJson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}