package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoginController{
    @FXML
    TextField userNameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Hyperlink signUpLink;
    private String userName,password;
    boolean loginDone=false;
    //    public void controlLogin(ActionEvent event) throws IOException {
//        while (!loginDone) {
//            loginButton.setOnAction(event1 -> {
//                try {
//                    handleButtonAction(event1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
    private void getInfo(){
        userName=userNameTextField.getText();
        password=passwordField.getText();
    }
    public void handleButtonAction(ActionEvent event) throws IOException, InterruptedException {
        getInfo();
        if(!userName.trim().isEmpty() && !password.trim().isEmpty()) {
            if (event.getSource() == loginButton) {
                System.out.println("button clicked");
                if (userName != null && password != null) {
                    boolean success = ChatClient.getInstance().login(userName, password);
                    if (success) {
                        changeScene("mainPage.fxml", event);
                    }
                }
            }
        }
    }

    private void changeScene(String sceneName,ActionEvent event) throws IOException {
        Parent view= FXMLLoader.load(getClass().getResource(sceneName));
        Scene scene=new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleSignUpLink(ActionEvent event) throws IOException {
        if(event.getSource()==signUpLink)
        changeScene("signUpPage.fxml",event);
    }
}
