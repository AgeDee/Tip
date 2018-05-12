package sample;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class settingsController {

    @FXML
    WebView avatarView;


    @FXML
    void initialize(){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img hight=133 width=133 src=\"https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png\"></img> ";
        webEngine.loadContent(avatarContent);
    }

    @FXML
    void selectAvatarAction() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj avatar...");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Zdjęcie (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(new Stage());

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "my_cloud_name",
                "api_key", "my_api_key",
                "api_secret", "my_api_secret"));

        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        System.out.println(uploadResult.toString());
    }

}
