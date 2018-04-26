package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    final ArrayList<ChatData> chatArray = new ArrayList<>();

    private FrameLayout chatFrame;
    private boolean isFrameLoaded;
    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private String[] allColumns = { ChatDatabaseHelper.Col_1,
            ChatDatabaseHelper.Col_2 };

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        dbHelper = new ChatDatabaseHelper(this);

        database = dbHelper.getWritableDatabase();

        readMessages();

        //Resources resources = getResources();
        final ListView listViewChat = findViewById(R.id.listViewChat);
        chatAdapter = new ChatAdapter(this);
        listViewChat.setAdapter(chatAdapter);
        final EditText editTextChat = findViewById(R.id.textChat);
        Button buttonSend = findViewById(R.id.sendChat);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatString = editTextChat.getText().toString();
                writeMessages(chatString);

                editTextChat.setText("");
            }
        });

        listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Object o = listViewChat.getItemAtPosition(position);
                ChatData str= (ChatData) o;
                Bundle data = new Bundle();

                data.putString("messageText", str.getMessage());
                data.putString("messageID", Integer.toString(position));
                data.putString("dataID", Long.toString(id));

                if(!isFrameLoaded) {        // phone
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(data);
                    startActivityForResult(intent, 5);
                } else {                    // tablet
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    MessageFragment f = new MessageFragment(1);
                    f.setArguments(data);

                    ft.replace(R.id.chatFrame, f);
                    ft.commit();
                }
            }
        });

        chatFrame = (FrameLayout)findViewById(R.id.chatFrame);

        isFrameLoaded = (chatFrame != null);


    }

    private void readMessages() {
        // read database and save messages into the array list
        Cursor cursor = database.query(ChatDatabaseHelper.TName,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.Col_2));
            long id = cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.Col_1));

            Log.i("Chat Window", "ID: " + cursor.getString( cursor.getColumnIndex(ChatDatabaseHelper.Col_1)) + " SQL MESSAGE:" + cursor.getString( cursor.getColumnIndex(ChatDatabaseHelper.Col_2)));

            ChatData data = new ChatData(message, id);

            chatArray.add(data);
            cursor.moveToNext();
        }

        Log.i("Chat Window", "Cursor’s column count =" + cursor.getColumnCount());

        for(int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i("Chat Window", "Column Name: " + cursor.getColumnName(i));
        }

        // close the cursor
        cursor.close();
    }

    private void writeMessages(String message) {
        ContentValues values = new ContentValues();

        values.put(ChatDatabaseHelper.Col_2, message);
        long id = database.insert(ChatDatabaseHelper.TName, null,
                values);

        ChatData data = new ChatData(message, id);
        chatAdapter.notifyDataSetChanged();
        chatArray.add(data);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 5) {
            Bundle extras = data.getExtras();
            int id = Integer.parseInt(extras.getString("id"));
            long dataid = Long.parseLong(extras.getString("dataID"));

            database.delete(ChatDatabaseHelper.TName, "_id=?",
                    new String[]{Long.toString(dataid)});

            chatArray.remove(id);
            chatAdapter.notifyDataSetChanged();
        }
    }

    public void deleteMessage(int id, long dataid) {
        database.delete(ChatDatabaseHelper.TName, "_id=?",
                new String[]{Long.toString(dataid)});

        chatArray.remove(id);
        chatAdapter.notifyDataSetChanged();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MessageFragment f = (MessageFragment)getFragmentManager().findFragmentById(R.id.chatFrame);

        ft.remove(f);
        ft.commit();

    }





    private class ChatAdapter extends ArrayAdapter<ChatData> {


        public ChatAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return chatArray.size();
        }



        public ChatData getItem(int position) {

            return chatArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }

            TextView message = result.findViewById((R.id.message_text));
            message.setText(getItem(position).getMessage());
            return result;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}