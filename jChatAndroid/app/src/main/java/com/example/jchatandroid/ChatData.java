package com.example.jchatandroid;

import java.util.ArrayList;

public class ChatData {
    private static ChatData instance=new ChatData();
    public ArrayList<Chat> chatDataList;
    public CustomAdapter customAdapter;
    public boolean newMessage=false;

    private ChatData(){
        chatDataList = new ArrayList<Chat>();
    }

    public static ChatData getInstance(){return instance;}
    public Chat addChat(Chat chat){
        chatDataList.add(chat);
        return chat;
    }

    public ArrayList<Chat> getChatDataList() {
        return chatDataList;
    }

}