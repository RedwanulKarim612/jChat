package com.example.jchatandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CurrentChatAdapter extends RecyclerView.Adapter<CurrentChatAdapter.ViewHolder> {
    Chat chat;

    CurrentChatAdapter(String name){
        this.chat=ChatData.getInstance().getChat(name);
    }
    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message=chat.getMessages().get(position);
        String sender = chat.getSender().get(position);
        if(sender.equals(chat.getFriendUserName())){
            holder.getFriendText().setText(message);
            holder.getYourText().setVisibility(View.INVISIBLE);
        }
        else{
            holder.getYourText().setText(message);
            holder.getFriendText().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chat.getChatLength();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView friendText;
        TextView yourText;

        public TextView getFriendText() {
            return friendText;
        }

        public TextView getYourText() {
            return yourText;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendText = itemView.findViewById(R.id.friendText);
            yourText = itemView.findViewById(R.id.yourText);
        }
    }
}
