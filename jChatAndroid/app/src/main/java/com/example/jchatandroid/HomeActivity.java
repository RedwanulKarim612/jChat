package com.example.jchatandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView= findViewById(R.id.recyclerView);
        customAdapter = new CustomAdapter(recyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("count", String.valueOf(customAdapter.getItemCount()));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(ChatData.getInstance().newMessage)
                    {
                        runOnUiThread(() -> customAdapter.notifyDataSetChanged());
                        Log.d("notified","notified");
                    }
                    ChatData.getInstance().newMessage=false;
                }
            }
        });
        t.start();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customAdapter.notifyDataSetChanged();
    }
}