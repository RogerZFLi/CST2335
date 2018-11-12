package com.example.amusu.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatWindow extends Activity {
   private ArrayList<String> chatMessages;
   private EditText editText;
   private Button send;
   private ChatDatabaseHelper cdh = new ChatDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        final ListView chatView = findViewById(R.id.chatView);

        ChatAdapter messageAdapter =new ChatAdapter( this, chatMessages );

        chatMessages = (ArrayList<String>)cdh.getAllMessages();
        editText = findViewById(R.id.editText);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessages.add(editText.getText().toString());
                cdh.insertMessage(editText.getText().toString());
                editText.setText(null);
            }
        });



        //in this case, “this” is the ChatWindow, which is-A Context object
        chatView.setAdapter (messageAdapter);

    }

    @Override
    protected void onDestroy() {
        cdh.getReadableDatabase().close();
        cdh.getWritableDatabase().close();
        super.onDestroy();

    }

    private class ChatAdapter extends BaseAdapter {
        private Context context;

        private ChatAdapter(Context context, ArrayList<String> chatMessage) {
            this.context = context;
            chatMessages = chatMessage;
        }

        @Override
        public int getCount(){
            return chatMessages.size();
        }

        @Override
        public String getItem(int position) {
            return chatMessages.get(position);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;

            if(position%2 == 0)

                result = inflater.inflate(R.layout.chat_row_incoming,null);

            else

                result = inflater.inflate(R.layout.chat_row_outgoing,null);
            TextView message = result.findViewById(R.id.message_text);

            message.setText( getItem(position) ); // get the string at position

            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    }
}
