package com.example.jchatandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    public ArrayList<Chat> dataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView friendName;
        private TextView chatText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendUserName);
            chatText = itemView.findViewById(R.id.chatText);
        }

        public TextView getFriendTextView() {
            return friendName;
        }

        public TextView getChatText() {
            return chatText;
        }
    }

    public CustomAdapter(){
        dataSet = ChatData.getInstance().getChatDataList();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = dataSet.get(position);
        holder.getFriendTextView().setText(chat.getFriendUserName());
        String message = chat.getMessages().get(chat.getChatLength()-1);
        String finalMessage;
        if(chat.sender.get(chat.getChatLength()-1).equals(chat.getFriendUserName()))  finalMessage = chat.getFriendUserName() + " : " + message;
        else finalMessage = "You : " + message;
        holder.getChatText().setText(finalMessage);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
