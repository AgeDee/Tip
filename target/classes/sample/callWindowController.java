package sample;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CallWindowController {

    @FXML
    WebView avatarView;

    @FXML
    void initialize(){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img hight=133 width=133 src=\"http://zadymki.pl/upload/photos/2018/01/gPdqJvLEbQwFm5SGH9Ym_30_b497cb3ee27436b158e886d3175e8bba_avatar_full.jpg\"></img> ";
        webEngine.loadContent(avatarContent);
    }

}
