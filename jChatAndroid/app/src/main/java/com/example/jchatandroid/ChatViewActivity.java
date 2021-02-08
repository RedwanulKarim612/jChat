package com.example.jchatandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class ChatViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private CurrentChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        Bundle bundle = getIntent().getExtras();
        String name= bundle.get("name").toString();
        chatAdapter = new CurrentChatAdapter(name);
        recyclerView = findViewById(R.id.chatRecyclerView);
        textView = findViewById(R.id.friendUserName);
        textView.setText(name);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}