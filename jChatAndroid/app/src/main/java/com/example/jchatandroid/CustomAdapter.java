package com.example.jchatandroid;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    public ArrayList<Chat> dataSet;
    private RecyclerView recyclerView;
    private final View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Chat chat= dataSet.get(position);
            Intent intent = new Intent(v.getContext(),ChatViewActivity.class);
            intent.putExtra("name",chat.getFriendUserName());
            v.getContext().startActivity(intent);
        }
    };

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

    public CustomAdapter(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        dataSet = ChatData.getInstance().getChatDataList();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = dataSet.get(position);
        holder.getFriendTextView().setText(chat.getFriendUserName());
        if(chat.getChatLength()!=0) {
            String message = chat.getMessages().get(chat.getChatLength() - 1);
            String finalMessage;
            if (chat.sender.get(chat.getChatLength() - 1).equals(chat.getFriendUserName()))
                finalMessage = chat.getFriendUserName() + " : " + message;
            else finalMessage = "You : " + message;
            holder.getChatText().setText(finalMessage);
        }
        else{
            String message = "Say hi to " + chat.getFriendUserName();
            holder.getChatText().setText(message);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
