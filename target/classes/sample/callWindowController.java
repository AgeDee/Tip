package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CallWindowController {

    @FXML
    Text targetUserText;

    @FXML
    WebView avatarView;

    String targetUserLogin;
    int targetUserId;

    UserDAO userDAO = new UserDAO();

    @FXML
    void initialize(){
    }

    public void setTargetUser(String targetUserLogin) {
        this.targetUserLogin = targetUserLogin;
        targetUserText.setText(targetUserLogin);
        targetUserId = userDAO.findByUserLogin(targetUserLogin).getUserId();
        setAvatar(AvatarManager.downloadAvatar(targetUserLogin));
    }

    void setAvatar(String url){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img style=\"width: 133;height: 133px;\" src=\" " + url + " \"></img> ";
        webEngine.loadContent(avatarContent);
    }
}
