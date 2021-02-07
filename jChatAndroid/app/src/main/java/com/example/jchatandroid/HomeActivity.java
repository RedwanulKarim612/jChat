package com.example.jchatandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView= findViewById(R.id.recyclerView);
        customAdapter = new CustomAdapter();
        ChatData.getInstance().customAdapter = this.customAdapter;
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("count", String.valueOf(customAdapter.getItemCount()));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(ChatData.getInstance().newMessage)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    ChatData.getInstance().newMessage=false;
                    Log.d("notified","notified");
                }
            }
        });
        t.start();
    }
}