package com.example.jchatandroid;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientListener extends Thread{
    private BufferedReader bufferedReader;
    String response;

    public ClientListener(BufferedReader bufferedReader) {
        this.bufferedReader=bufferedReader;
    }

    public String getResponse() {
        if(response!=null) return response;
        return "no response";
    }

    @Override
    public void run() {
        try {
            while((response = bufferedReader.readLine())!=null){
                String []tokens=response.split(" ",4);
                if(tokens!=null && tokens.length>0){
                    String cmd=tokens[0];
                    if(cmd.equals("msg")){
                        Log.d("msg",response);
                        boolean found=false;
                        for(Chat c:ChatData.getInstance().getChatDataList()){
                            if(c.getFriendUserName().equals(tokens[1])) {
                                c.addMessage(tokens[3], tokens[2]);
                                found=true;
                                break;
                            }
                        }
                        if(!found){
                            Chat newChat=new Chat(tokens[1]);
                            newChat.addMessage(tokens[3],tokens[2]);
                            ChatData.getInstance().getChatDataList().add(newChat);
//                            if(controller==null)
//                                System.out.println("null controller");
                        }
                        }
                    }
                }
                response=null;
            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}