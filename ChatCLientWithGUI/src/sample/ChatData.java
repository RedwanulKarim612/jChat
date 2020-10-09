package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatData {
    private static ChatData instance=new ChatData();
    private ObservableList<Chat>chatDataList;



    public static ChatData getInstance(){return instance;}
    public Chat addChat(Chat chat){
        chatDataList.add(chat);
        return chat;
    }

    public ObservableList<Chat> getChatDataList() {
        return chatDataList;
    }
    public void loadChatData(){
        chatDataList= FXCollections.observableArrayList();
    }
}
