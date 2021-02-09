package com.example.jchatandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class ChatViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private CurrentChatAdapter chatAdapter;
    private EditText messageText;
    private Button sendButton;
    private String friendName;
    private Chat chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        Bundle bundle = getIntent().getExtras();
        friendName= bundle.get("name").toString();
        chat = ChatData.getInstance().getChat(friendName);
        chatAdapter = new CurrentChatAdapter(chat);
        recyclerView = findViewById(R.id.chatRecyclerView);
        textView = findViewById(R.id.friendUserName);
        messageText = findViewById(R.id.messageText);
        sendButton = findViewById(R.id.sendButton);
        textView.setText(friendName);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
        ((LinearLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);


        chatAdapter.notifyDataSetChanged();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(chat.newMessage){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                                chat.newMessage=false;
                                Log.d("notifiedinuser","notified");
                            }
                        });
                    }
                }
            }
        });

        t.start();
//        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v,
//                                       int left, int top, int right, int bottom,
//                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (bottom < oldBottom) {
//                    recyclerView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView.smoothScrollToPosition(
//                                    recyclerView.getAdapter().getItemCount() - 1);
//                        }
//                    }, 100);
//                }
//            }
//        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        chatAdapter.notifyDataSetChanged();
    }

    public void handleSendButton(View view) {
        String message = messageText.getText().toString();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!message.trim().isEmpty()) {
                        ChatClient.getInstance().send(friendName, message);
                        chat.newMessage = true;
                        chat.getMessages().add(message);
                        messageText.getText().clear();
                        chat.getSender().add(ChatClient.getInstance().getUserName());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.notifyItemInserted(chatAdapter.getItemCount());
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}