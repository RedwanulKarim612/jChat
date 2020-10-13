package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


public class MainPageController {
    @FXML
    public TextField searchText;
    @FXML
    public Button searchButton;
    @FXML
    public Button logoffButton;
    @FXML
    public ListView<Chat> chatList;
    @FXML
    public Label friendUserName;
    private List<Chat>chats ;
    @FXML
    public Button sendButton;
    @FXML
    private TextField messageField;
    @FXML
    public VBox chatVBox;
    @FXML
    public ScrollPane scrollPane;

    public ListView<Chat> getChatList() {
        return chatList;
    }

    public void initialize() throws IOException {

        ChatClient.getInstance().clientListener.controller=MainPageController.this;
        chatVBox.setSpacing(10);
        scrollPane.setFitToWidth(true);
        chatList.setCellFactory(new Callback<ListView<Chat>, ListCell<Chat>>() {
            @Override
            public ListCell<Chat> call(ListView<Chat> chatListView) {
                ListCell<Chat> cell = new ListCell<>() {
                    protected void updateItem(Chat item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setText(null);
                        else {
                            setText(item.getFriendUserName());
                        }
                    }
                };
                return cell;
            }
        });
    }
    public void handleSearch(ActionEvent event) throws IOException {
        if(event.getSource().equals(searchButton)) {
            String toSearch = searchText.getText().trim();
            System.out.println(toSearch);
            if (!toSearch.isEmpty()) {
                if (chatList.getSelectionModel().getSelectedItem() != null && chatList.getSelectionModel().getSelectedItem().getFriendUserName().equals(toSearch)) {
                    System.out.println("same");
                    return;
                }
                for (Chat chat : ChatData.getInstance().getChatDataList()) {
                    if (chat.getFriendUserName().equals(toSearch)) {
                        chatList.getSelectionModel().select(chat);
                        chatVBox.getChildren().clear();
                        loadMessageToVBox(chat);
                        return;
                    }
                }

                    if (ChatClient.getInstance().searchUser(toSearch)) {
                        System.out.println("user found");
                        Chat newChat = new Chat(toSearch);
                        chatVBox.getChildren().clear();
                        chatList.getItems().add(newChat);
                        friendUserName.setText(toSearch);
                        chatList.getSelectionModel().select(newChat);
                        ChatData.getInstance().addChat(newChat);
                    }

            }
        }
    }
    public void handleSendMessage(ActionEvent event) throws IOException {
        if(event.getSource().equals(sendButton)){
            String toSend=messageField.getText().trim();
            if(toSend.isEmpty()) return ;
            System.out.println(toSend);
            Chat receiver=chatList.getSelectionModel().getSelectedItem();
//            System.out.println(receiver.getFriendUserName());
            ChatClient.getInstance().send(receiver.getFriendUserName(),toSend);
            Label newLabel=new Label(toSend);
            newLabel.getStyleClass().clear();
            newLabel.getStylesheets().add(getClass().getResource("/css files/messageStyle.css").toExternalForm());
            newLabel.getStyleClass().add("messageStyle");
            HBox hbox=new HBox();
//            newLabel.setMaxWidth(400);
            newLabel.setWrapText(true);
            newLabel.setAlignment(Pos.BASELINE_RIGHT);
            friendUserName.setText(receiver.getFriendUserName());
            newLabel.setAlignment(Pos.BASELINE_RIGHT);
            hbox.getChildren().add(newLabel);
            hbox.setAlignment(Pos.BASELINE_RIGHT);
            chatVBox.getChildren().add(hbox);
            messageField.clear();
            for(Chat c:ChatData.getInstance().getChatDataList()){
                if(c.getFriendUserName().equals(receiver.getFriendUserName())){
                    c.addMessage(toSend,receiver.getFriendUserName());
                    break;
                }
            }
        }
    }

    public void addMessage(String str){
        System.out.println("adding message");
        Label label=new Label(str);
        label.getStyleClass().clear();
        label.getStylesheets().add(getClass().getResource("/css files/messageStyle.css").toExternalForm());
//        label.setMaxWidth(400);
        label.setWrapText(true);

        label.getStyleClass().add("messageStyle");
        HBox hBox=new HBox();
        hBox.getChildren().add(label);
        hBox.setAlignment(Pos.BASELINE_LEFT);
        chatVBox.getChildren().add(hBox);
    }
    public void handleClickList(MouseEvent event){
        Chat chat1=chatList.getSelectionModel().getSelectedItem();
        if(chat1==null) return ;
        if(chat1.getFriendUserName().equals(friendUserName.getText())) return ;
        friendUserName.setText(chat1.getFriendUserName());
        chatVBox.getChildren().clear();
        loadMessageToVBox(chat1);
    }

    private void loadMessageToVBox(Chat chat) {
        for (int i = 0; i < chat.messages.size(); i++) {
            String str = chat.messages.get(i);
            Label msg = new Label(str);
            msg.getStyleClass().clear();
            msg.getStylesheets().add(getClass().getResource("/css files/messageStyle.css").toExternalForm());
            msg.getStyleClass().add("messageStyle");
            msg.setWrapText(true);
            HBox hBox = new HBox();
            hBox.getChildren().add(msg);
            if (chat.sender.get(i).equals(chat.getFriendUserName())) hBox.setAlignment(Pos.BASELINE_LEFT);
            else hBox.setAlignment(Pos.BASELINE_RIGHT);
            chatVBox.getChildren().add(hBox);
            scrollPane.vvalueProperty().bind(chatVBox.heightProperty());
        }
    }

    public void handleLogOff(MouseEvent event) throws IOException {
        ChatData.getInstance().getChatDataList().clear();
        Parent view= FXMLLoader.load(getClass().getResource("/fxml files/loginPage.fxml"));
        Scene scene=new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
