package sample;

import javafx.event.ActionEvent;
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

import java.io.IOException;


public class SignUpPageController {
    @FXML
    TextField userNameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button signUpButton;
    @FXML
    Hyperlink loginLink;
    private String userName,password;

    public void handleButtonAction(ActionEvent event) throws IOException {
        getInfo();
        if(!userName.trim().isEmpty() && !password.trim().isEmpty()) {
            if (event.getSource() == signUpButton) {
                System.out.println("button clicked");
                if (userName != null && password != null) {
                    boolean success = ChatClient.getInstance().signUp(userName, password);
                    if (success) {
                        changeScene("mainPage.fxml", event);
                    }
                }
            }
        }
    }
    private void getInfo(){
        userName=userNameTextField.getText();
        password=passwordField.getText();
    }

    private void changeScene(String sceneName,ActionEvent event) throws IOException {
        Parent view= FXMLLoader.load(getClass().getResource(sceneName));
        Scene scene=new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleLoginLink(ActionEvent event) throws IOException {
        if(event.getSource()==loginLink){
            changeScene("loginPage.fxml",event);
        }
    }
}
