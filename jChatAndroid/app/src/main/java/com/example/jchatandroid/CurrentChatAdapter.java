package com.example.jchatandroid;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CurrentChatAdapter extends RecyclerView.Adapter<CurrentChatAdapter.ViewHolder> {
    Chat chat;

    CurrentChatAdapter(String name){
        this.chat=ChatData.getInstance().getChat(name);
    }

    public CurrentChatAdapter(Chat chat) {
        this.chat=chat;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_text_layout, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_text_layout, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        String s=chat.getSender().get(position);
        if(s==null) Log.d("nullex","null");
        if(s.equals(chat.getFriendUserName())) return 0;
        else return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message=chat.getMessages().get(position);
        holder.text.setText(message);
    }

    @Override
    public int getItemCount() {
        return chat.getChatLength();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
        }
    }
}