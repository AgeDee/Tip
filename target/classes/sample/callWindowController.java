package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;

public class CallWindowController {

    @FXML
    Text targetUserText;

    @FXML
    Text conversationTime;

    @FXML
    WebView avatarView;

    @FXML
    Button endCallButton;

    public String targetUserLogin;
    int targetUserId;
    public String countedTime;

    //Zmienne timera
    private AnimationTimer timer;
    private int seconds;
    private int minutes = 0;

    UserDAO userDAO = new UserDAO();

    @FXML
    void initialize(){
        timer = new AnimationTimer() {

            private long lastTime = 0;


            @Override
            public void handle(long now) {
                if (lastTime != 0) {
                    if (now > lastTime + 1_000_000_000) {
                        if(seconds < 59) {
                            seconds++;
                            String form_minutes = String.format("%02d", minutes);
                            String form_seconds = String.format("%02d", seconds);
                            conversationTime.setText("Czas rozmowy: 00:" + form_minutes + ":" + form_seconds); //Czas rozmowy
                            lastTime = now;
                        }else {
                            seconds = 0;
                            minutes++;
                            String form_minutes = String.format("%02d", minutes);
                            String form_seconds = String.format("%02d", seconds);
                            conversationTime.setText("Czas rozmowy: 00:" + form_minutes + ":" + form_seconds);
                            lastTime = now;
                        }
                    }
                } else {
                    lastTime = now;

                }
            }

            @Override
            public void stop() {
                super.stop();
                countedTime = "0h " + minutes + "min " + seconds + "sec";
                lastTime = 0;
                seconds = 0;
                minutes = 0;
            }
        };

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

    public void startTimer(){
        timer.start();
    }

    public void stopTimer(){
        timer.stop();
    }
}
