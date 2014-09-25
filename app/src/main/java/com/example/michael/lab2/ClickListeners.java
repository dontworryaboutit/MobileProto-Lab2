package com.example.michael.lab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class ClickListeners {

    public static AlertDialog.Builder getEditMessageDialogBuilder(final Activity activity, final ChatAdapter chatAdapter, final long id) {
        final EditText inputMessage = new EditText(activity);
        inputMessage.setText(chatAdapter.getChatMessage((int) id));
        return new AlertDialog.Builder(activity)
                .setTitle("Edit Message #" + id)
                .setMessage("Modify message information below:")
                .setView(inputMessage)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newMessage = inputMessage.getText().toString();
                        if (newMessage.equals("")) {
                            Toast.makeText(activity, "Discarded; can't be blank!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        chatAdapter.updateChatMessage((int) id, newMessage);
                        dialogInterface.dismiss();
                    }
                });
    }

    public static View.OnClickListener sendButtonListener(final Activity activity, final ChatAdapter chatAdapter){
        // stuff to do when button is clicked
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = ((EditText) activity.findViewById(R.id.main_input_entry));
                if (input.getText().toString().equals("")){
                    Toast.makeText(activity, "You didn't type anything in!", Toast.LENGTH_SHORT).show();
                    return;
                }
                chatAdapter.addChat(new ChatModel(MyActivity.username, input.getText().toString()));
                input.setText("");
            }
        };
    }

    public static AdapterView.OnItemClickListener clickChatListener(final Activity activity,
                                                                    final ChatAdapter chatAdapter){
        // stuff to do when chat message is clicked
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                final ChatModel chat = chatAdapter.getChat((int) id);
                new AlertDialog.Builder(activity)
                        .setTitle("Message #" + id)
                        .setMessage("What would you like to do with this message")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                getEditMessageDialogBuilder(activity, chatAdapter, id).show();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (chat == null) {
                                    Toast.makeText(activity, "Discarded; invalid index!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                chatAdapter.deleteChat(chat);
                                Toast.makeText(activity, "Deleted message #" + id + "!", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        };
    }

    public static void changeUsernameListener(final Activity activity){
        // stuff to do when change username button is clicked
        // create dialogue box of AlertDialog type (predefined)
        final EditText input = new EditText(activity);
        new AlertDialog.Builder(activity)
                .setTitle("Change Username")
                .setMessage("This is how you will show up to others.")
                .setView(input)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = input.getText().toString();
                        if (newName.equals("")) {
                            Toast.makeText(activity, "Discarded; can't be blank!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MyActivity.username = newName;
                        Toast.makeText(activity, "New username: " + newName, Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}