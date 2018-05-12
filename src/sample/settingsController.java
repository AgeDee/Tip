package sample;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class settingsController {

    @FXML
    WebView avatarView;

    @FXML
    void initialize(){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img hight=133 width=133 src=\"https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png\"></img> ";
        webEngine.loadContent(avatarContent);
    }

}
