package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;

public class mainController {

    @FXML
    ListView<String> contactsList;

    @FXML
    WebView avatarView;

    @FXML
    Button connectButton;

    @FXML
    Button blockButton;

    @FXML
    Button logoutButton;

    @FXML
    Button logButton;

    @FXML
    Button blockedButton;

    @FXML
    Button settingsButton;

    @FXML
    Button searchButton;

    @FXML
    Button deleteButton;


    @FXML
    void initialize(){
        addResultItem("Testov");
        addResultItem("m_b");
        addResultItem("Jabollo13");
        addResultItem("Ćma");

        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img hight=133 width=133 src=\"https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png\"></img> ";
        webEngine.loadContent(avatarContent);

    }

    @FXML
    void logoutAction(){

    }

    @FXML
    void blockAction(){

    }

    @FXML
    void deleteAction(){

    }

    @FXML
    void logAction(){

    }

    @FXML
    void settingsAction(){

    }

    @FXML
    void searchAction(){

    }

    @FXML
    void connectAction(){

    }

    @FXML
    void blockedAction(){

    }

    void addResultItem(String name){
        contactsList.getItems().add(name);
    }

    void clearResultList(){
        contactsList.getItems().clear();
    }

}
