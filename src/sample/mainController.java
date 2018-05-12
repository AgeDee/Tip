package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList (
                "Testov", "m_b", "Jabollo13", "Ćma");
        contactsList.setItems(items);

        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img hight=133 width=133 src=\"https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png\"></img> ";
        webEngine.loadContent(avatarContent);

    }

}
