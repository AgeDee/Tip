package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ConnectionWindowController {

    @FXML Text callerNameText;

    @FXML public Button getCallButton;
    @FXML public Button rejectCallButton;

    UserDAO userDAO = new UserDAO();

    @FXML
    void initialize(){

    }

//    @FXML
//    void getCallButtonAction(){
//
//    }
//
//    @FXML
//    void rejectCallButtonAction(){
//
//    }

    public void setCallerLogin(String callerLogin){
        callerNameText.setText(callerLogin + " dzwoni");
    }





}
