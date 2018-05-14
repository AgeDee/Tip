package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class mainController {

    @FXML
    ListView<String> contactsList;

    @FXML
    Text userText;

    @FXML
    Text selectedText;

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

    String userId = CurrentUser.getUserId();

    @FXML
    void initialize(){
        setAvatar(AvatarManager.downloadAvatar(userId));
        userText.setText(userId);

        //temp
        addResultItem("Testov");
        addResultItem("m_b");
        addResultItem("Jabollo13");
        addResultItem("Ćma");


    }

    @FXML
    void logoutAction(){
        System.out.println("Wyloguj");
    }

    @FXML
    void blockAction(){
        System.out.println("Zablokuj");
    }

    @FXML
    void deleteAction(){
        System.out.println("Usuwanie");
    }

    @FXML
    void logAction(){
        System.out.println("Historia");
    }

    @FXML
    void settingsAction() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Ustawienia");
        stage.setScene(new Scene(root));

        stage.setOnCloseRequest(event -> {
            System.out.println("Wczytywanie avatara.");
            setAvatar(AvatarManager.downloadAvatar(userId));
        });

        stage.show();


    }

    @FXML
    void searchAction(){
        System.out.println("Wyszukiwanie");
    }

    @FXML
    void connectAction(){
        System.out.println("Połącz");
    }

    @FXML
    void blockedAction(){
        System.out.println("Zablokowani");
    }

    @FXML
    void contactListAction(){

        if (contactsList.getSelectionModel().getSelectedIndex() != -1) {

            String selectedContent = contactsList.getSelectionModel().getSelectedItem();
            System.out.println(selectedContent);
            selectedText.setText(selectedContent);

        } else {
            System.out.println("Nie wybrano elementu!");
        }
    }

    void addResultItem(String name){
        contactsList.getItems().add(name);
    }

    void clearResultList(){
        contactsList.getItems().clear();
    }

    void setAvatar(String url){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img style=\"width: 133;height: 133px;\" src=\" " + url + " \"></img> ";
        webEngine.loadContent(avatarContent);
    }

}
