package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;

public class BlockedUsersController {

    @FXML
    Button closeButton;

    @FXML
    void initialize(){

    }

    @FXML
    void closeAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
