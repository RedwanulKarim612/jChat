package sample;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    Label errorLabel;
    private String userName,password;

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
                        changeScene("/fxml files/mainPage.fxml", event);
                    }
                    else{
                        PauseTransition visiblePause = new PauseTransition(
                                Duration.seconds(2)
                        );
                        errorLabel.setVisible(true);
                        visiblePause.setOnFinished(
                                newEvent -> errorLabel.setVisible(false)
                        );
                        visiblePause.play();
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
        changeScene("/fxml files/signUpPage.fxml",event);
    }
}
