package com.example.jchatandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Button searchButton;
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView= findViewById(R.id.recyclerView);
        searchButton = findViewById(R.id.searchButton);
        searchText = findViewById(R.id.searchText);
        customAdapter = new CustomAdapter(recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("count", String.valueOf(customAdapter.getItemCount()));
        Thread t = new Thread(() -> {
            while(true){
                if(ChatData.getInstance().newMessage)
                {
                    runOnUiThread(() -> customAdapter.notifyDataSetChanged());
                    Log.d("notified","notified");
                }
                ChatData.getInstance().newMessage=false;
            }
        });
        t.start();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customAdapter.notifyDataSetChanged();
    }

    public void handleLogOutButton(View view) {
        Thread t=new Thread(
                () -> {
                    try {
                        ChatClient.getInstance().logoff();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        t.start();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void handleSearchButton(View view) {
        String search = searchText.getText().toString();
        if(search.trim().isEmpty()) return;
        Thread t= new Thread(()->{
                    Chat chat = ChatData.getInstance().getChat(search);
                    if(chat!=null) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(HomeActivity.this, ChatViewActivity.class);
                            intent.putExtra("name", search);
                            startActivity(intent);
                        });
                    }
                }
        );
        t.start();
        Thread t2=new Thread(() -> {
            if(ChatData.getInstance().getChat(search) == null){
                try {
                    if(ChatClient.getInstance().searchUser(search)){
                        Chat newChat = new Chat(search);
                        ChatData.getInstance().addChat(newChat);
                        runOnUiThread(() -> {
                            Intent intent = new Intent(HomeActivity.this, ChatViewActivity.class);
                            intent.putExtra("name", search);
                            startActivity(intent);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }
}