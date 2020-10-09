package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread{
    private BufferedReader bufferedReader;
    String response;
    public MainPageController controller;

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
//                        FXMLLoader fxmlLoader=new FXMLLoader();
//                        fxmlLoader.setLocation(getClass().getResource("mainPage.fxml"));
//                        Parent root=fxmlLoader.load();
//
//                        MainPageController controller=fxmlLoader.getController();
                        if(controller!=null)
                        if(controller.friendUserName.getText().equals(tokens[1])){
                            Platform.runLater(()->{
                                controller.addMessage(tokens[3]);
                            });

//                            controller.chatVBox.getChildren().add(new Label(tokens[3]));
                        }
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

//                            newChat.addMessage(tokens[3],tokens[2]);
                            Platform.runLater(()->{
                                controller.chatList.getItems().add(newChat);
                            });


                            ChatData.getInstance().getChatDataList().add(newChat);
//                            if(controller==null)
//                                System.out.println("null controller");
                        }
                    }
                }
                response=null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
