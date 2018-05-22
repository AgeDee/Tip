package sample;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingsController {

    @FXML
    WebView avatarView;


    String userId = CurrentUser.getUserLogin();

    @FXML
    void initialize() {
        setAvatar(AvatarManager.downloadAvatar(userId));

    }

    @FXML
    void selectAvatarAction() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj avatar...");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Zdjęcie (*.jpg;*.png)", "*.jpg;*png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null) {
            AvatarManager.uploadAvatar(file, userId);
            setAvatar(AvatarManager.downloadAvatar(userId));
            System.out.println("Ustawiono nowy avatar");
        }else {
            System.out.println("Nie wczytano pliku");
        }
    }

    void setAvatar(String url){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img style=\"width: 133;height: 133px;\" src=\" " + url + " \"></img> ";
        webEngine.loadContent(avatarContent);
    }
}
