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

public class MainController {

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
    void logoutAction() throws IOException {
        System.out.println("Wyloguj");

        Stage stage = (Stage) userText.getScene().getWindow();
        stage.close();

        CurrentUser.setUserId("");

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);
        stage.show();
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
    void logAction() throws IOException {
        System.out.println("Historia");

        Parent root = FXMLLoader.load(getClass().getResource("log.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Historia");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        stage.show();

    }

    @FXML
    void settingsAction() throws IOException {
        System.out.println("Ustawienia");

        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Ustawienia");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Wczytywanie avatara.");
            setAvatar(AvatarManager.downloadAvatar(userId));
        });

        stage.show();


    }

    @FXML
    void searchAction() throws IOException {
        System.out.println("Wyszukiwanie");

        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Wyszukiwanie");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Odświeżanie listy kontaktów.");
            //todo
        });

        stage.show();
    }

    @FXML
    void connectAction(){
        System.out.println("Połącz");
    }

    @FXML
    void blockedAction() throws IOException {
        System.out.println("Zablokowani");

        Parent root = FXMLLoader.load(getClass().getResource("blockedUsers.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Zablokowani użytkownicy");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Aktualizacja tabeli z userami zablokowanymi.");
            //todo
        });

        stage.show();
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
