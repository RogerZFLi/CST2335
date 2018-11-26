package com.example.amusu.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatWindow extends Activity {
   private final String ACTIVITY_NAME = "ChatWindow";
   private ListView chatView;
   private ArrayList<Message> chatMessages;
   private EditText editText;
   private Button send;
   private ChatDatabaseHelper cdh = new ChatDatabaseHelper(this);
   private boolean frameLayoutExists;
   private Cursor cursor;
   private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Log.i(ACTIVITY_NAME, "in onCreate");

        chatView = findViewById(R.id.chatView);
        frameLayoutExists = findViewById(R.id.chat_frame) != null;

        editText = findViewById(R.id.editText);
        send = findViewById(R.id.send);
        cursor = cdh.c;
        chatMessages = (ArrayList<Message>)cdh.getAllMessages();
        messageAdapter =new ChatAdapter( this);
        chatView.setAdapter (messageAdapter);
        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (frameLayoutExists) {
                    MessageFragment mf = new MessageFragment();
                    Bundle fragmentArgs = new Bundle();
                    fragmentArgs.putLong("ID", id);
                    fragmentArgs.putString("message", chatMessages.get(position).getMessage());
                    mf.setArguments(fragmentArgs);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.chat_frame, mf);
                    ft.commit();

                } else {
                    Intent i = new Intent(ChatWindow.this, MessageDetails.class);
                    i.putExtra("ID", id);
                    i.putExtra("message", chatMessages.get(position).getMessage());
                    startActivityForResult(i, 11);
                }
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = editText.getText().toString();
                Message ms = cdh.insertMessage(msgText);
                if(ms!=null){
                    chatMessages.add(ms);
                    messageAdapter.notifyDataSetChanged();

                }
                editText.setText(null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11){
            if(resultCode ==RESULT_OK){
                Long messageID=data.getLongExtra("delete",-1);
                deleteMessage(messageID);
                Log.i(ACTIVITY_NAME,messageID + " is deleted");
            }
        }
    }

    @Override
    protected void onDestroy() {
        cdh.close();
        super.onDestroy();

    }

    private class ChatAdapter extends ArrayAdapter<Message> {
        private ChatAdapter(Context context) {
            super(context,0);
        }

        @Override
        public int getCount(){
            return chatMessages.size();
        }

        @Override
        public Message getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        public View getView(int position, @Nullable View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);
            TextView message = result.findViewById(R.id.message_text);
            message.setText( Objects.requireNonNull(getItem(position)).getMessage());
            return result;
        }

        @Override
        public long getItemId(int position) {
            return Objects.requireNonNull(getItem(position)).getId();
        }
    }

    public List<Message> getAllMessages() {

        final List<Message> messages = new ArrayList<>();
        final SQLiteDatabase db = cdh.getWritableDatabase();

        final Cursor cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        final int cIdIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID);
        final int cMessageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);

        Log.i(ACTIVITY_NAME, "Coursor column count: " + cursor.getColumnCount());
        Log.i(ACTIVITY_NAME, "Cursor Col: " + cursor.getColumnName(cIdIndex));
        Log.i(ACTIVITY_NAME, "Cursor Column: " + cursor.getColumnName(cMessageIndex));

        this.cursor = cursor;

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cIdIndex);
            String txt = cursor.getString(cMessageIndex);

            final Message msg = new Message(id, txt);
            messages.add(msg);
            Log.i(ACTIVITY_NAME, "SQL message text: " + txt);
        }
        return messages;
    }

    protected  void deleteMessage(long id){
        SQLiteDatabase db = cdh.getWritableDatabase();
        db.execSQL("DELETE FROM "+ ChatDatabaseHelper.TABLE_NAME+ " WHERE " + ChatDatabaseHelper.KEY_ID +" = " +id);
        chatMessages=new ArrayList<>();
        chatMessages =(ArrayList<Message>)getAllMessages();
        messageAdapter.notifyDataSetChanged();
    }
}
