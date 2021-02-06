package com.example.jchatandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<Chat> dataSet;

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
        holder.getFriendTextView().setText(dataSet.get(position).getFriendUserName());
        holder.getChatText().setText(dataSet.get(position).getMessages().get(0));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
