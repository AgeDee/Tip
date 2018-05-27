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
import java.util.ArrayList;
import java.util.List;

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

    UserDAO userDAO = new UserDAO();
    ContactDAO contactDAO = new ContactDAO();
    BlockedUserDAO blockedUserDAO = new BlockedUserDAO();

    String userLogin = userDAO.findByUserLogin(CurrentUser.getUserLogin()).getLogin();
    int userId = userDAO.findByUserLogin(userLogin).getUserId();

    @FXML
    void initialize(){
        setAvatar(AvatarManager.downloadAvatar(userLogin));
        userText.setText(userLogin);
        selectedText.setText("-");

        reloadContactList();
    }

    @FXML
    void logoutAction() throws IOException {
        System.out.println("Wyloguj");

        Stage stage = (Stage) userText.getScene().getWindow();
        stage.close();

        CurrentUser.setUserLogin("");

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void blockAction(){
        System.out.println("Zablokuj");

        if (contactsList.getSelectionModel().getSelectedIndex() != -1) {

            String selectedUser = contactsList.getSelectionModel().getSelectedItem();
            int selectedUserId = userDAO.findByUserLogin(selectedUser).getUserId();

            BlockedUser blockedUser = new BlockedUser(userId, selectedUserId, "current_timestamp"); //data tutaj nie gra roli
            blockedUserDAO.create(blockedUser);
            System.out.println("Użytkownik " + selectedUser + " został wpisany na listę zablokowanych użytkowników.");
            reloadContactList();

        } else {
            System.out.println("Nie wybrano elementu!");
        }
    }

    @FXML
    void deleteAction(){
        System.out.println("Usuwanie");

        if (contactsList.getSelectionModel().getSelectedIndex() != -1) {

            String selectedUser = contactsList.getSelectionModel().getSelectedItem();
            int selectedUserId = userDAO.findByUserLogin(selectedUser).getUserId();

            List<Contact> contactListResult = contactDAO.findByUserId(userId);

            for(Contact c : contactListResult){
                if(c.getContact_id() == selectedUserId){
                    contactDAO.deleteContactById(c.getId());
                    System.out.println("Login usuniętego użytkownika: " + userDAO.findByUserId(c.getContact_id()).getLogin());
                    reloadContactList();
                }
            }

        } else {
            System.out.println("Nie wybrano elementu!");
        }

    }

    @FXML
    void logAction() throws IOException {
        System.out.println("Historia");


        FXMLLoader loader = new FXMLLoader(getClass().getResource("log.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Historia");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        LogController controller = loader.getController();
        controller.loadList();

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
            setAvatar(AvatarManager.downloadAvatar(userLogin));
        });

        stage.show();
    }

    @FXML
    void searchAction() throws IOException {
        System.out.println("Wyszukiwanie");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Wyszukiwanie");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        SearchController controller = loader.getController();
        controller.setStageAndSetupListeners(stage);

        stage.setOnCloseRequest(event -> {
            System.out.println("Odświeżanie listy kontaktów.");
            reloadContactList();
        });

        stage.show();
    }

    @FXML
    void connectAction() throws IOException {
        System.out.println("Połącz");

        if (contactsList.getSelectionModel().getSelectedIndex() != -1) {

            String selectedUser = contactsList.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("callWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Połączenie");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("sample/style.css");
            stage.setScene(scene);

            CallWindowController controller = loader.getController();
            controller.setTargetUser(selectedUser);

            stage.setOnCloseRequest(event -> {
                System.out.println("Zakończenie połączenia");
                //todo rozłączanie po kliknięciu krzyżyka
            });

            stage.show();


        } else {
            System.out.println("Nie wybrano elementu!");
        }
    }

    @FXML
    void blockedAction() throws IOException {
        System.out.println("Zablokowani");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("blockedUsers.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Zablokowani użytkownicy");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        BlockedUsersController controller = loader.getController();
        controller.setStageAndSetupListeners(stage);

        stage.setOnCloseRequest(event -> {
            System.out.println("Aktualizacja listy z kontaktami.");
            reloadContactList();
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

    void reloadContactList(){
        List<Contact> contactListResult = contactDAO.findByUserId(userId);
        List<BlockedUser> blockedUsersResult = blockedUserDAO.getByUserId(userId);
        List<Integer> blockedUserIdsList = new ArrayList();

        for(BlockedUser b : blockedUsersResult){
            blockedUserIdsList.add(b.getBlockedId());
        }

        clearResultList();

        for(Contact c : contactListResult){
            if(!blockedUserIdsList.contains(c.getContact_id())){
                addResultItem(String.valueOf(userDAO.findByUserId(c.getContact_id()).getLogin()));
            }else{
                System.out.println("Wykryto zablokowanego usera: " + userDAO.findByUserId(c.getContact_id()).getLogin());
            }
        }
    }

}
