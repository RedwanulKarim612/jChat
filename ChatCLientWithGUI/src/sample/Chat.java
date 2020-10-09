package sample;

import java.util.ArrayList;

public class Chat {
    private String friendUserName;
    public ArrayList<String> messages;
    public ArrayList<String> sender;
    Chat(String friendUserName){
        this.friendUserName=friendUserName;
        this.messages=new ArrayList<>();
        this.sender=new ArrayList<>();
    }

    public String getFriendUserName() {
        return friendUserName;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ArrayList<String> getSender() {
        return sender;
    }

    public void setFriendUserName(String friendUserName) {
        this.friendUserName = friendUserName;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public void setSender(ArrayList<String> sender) {
        this.sender = sender;
    }

    public void addMessage(String token,String msgFrom) {
        messages.add(token);
        sender.add(msgFrom);
    }
}
