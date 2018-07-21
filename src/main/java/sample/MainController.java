package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    //Utworzenie obiektu voipConnection w celu wywoływania funkcji do połączenia w dalszej czesci programu
    public static VoipConnection voipConnection = new VoipConnection();

    //Statyczne zmienne wykorzystywane w innych klasach programu

    //Wykorzystywana zmienna do zamykania okna gdy te zostanie zamknięte przez naszego rozmówce
    public static Stage callWindowStage;
    public static MessageCommunicationClientClass mainMessageClient;

    @FXML
    void initialize(){
        setAvatar(AvatarManager.downloadAvatar(userLogin));
        userText.setText(userLogin);
        selectedText.setText("-");

        //Odpalenie funkcji receiveCall() w celu nasłuchiwania na okreslonym ip i porcie

//        new Thread(() -> {
//            System.out.println("Thread started!!!!");
//            voipConnection.receiveCall();
//        }).start();

        //Odpalenie servera do komunikacji pomiędzy clientami
        new Thread(() -> {
            System.out.println("Thread started!!!!!");
            try {
                MessageCommunicationClass messageServer = new MessageCommunicationClass();
                messageServer.startMsgServer("192.168.0.28", 8888);
                //messageServer.startMsgServer("192.168.0.15", 8888);
            }catch (Exception ex){
                System.out.println("Błąd w funkcji MainController::initialize()");
                System.out.println(ex);
            }

        }).start();

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("callToUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Łączę ...");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("sample/style.css");
            stage.setScene(scene);

            CallToUserController callToUserController = loader.getController();
            callToUserController.setInfomrationalText(selectedUser);

//            stage.setOnShowing(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent event) {
//
//                        new Thread(() -> {
//                            try {
//                                handleConnection(stage);
//                            }catch(Exception ex){
//                                System.out.println("Exception in Stage::setOnShowing connectAction class");
//                                System.out.println(ex);
//                            }
//                        });
//                }
//            });

            new Thread(() -> {

                try {
                        handleConnection(stage);
                    }catch(Exception ex){
                        System.out.println("Exception in Stage::setOnShowing connectAction class");
                        System.out.println(ex);
                    }
            }).start();

            stage.show();






        } else {
            System.out.println("Nie wybrano elementu!");
        }
    }

    private void handleConnection(Stage stageToClose) throws IOException {
        //Początek kodu wykonywanego po stage.show

        //Tworzymy clienta do wymiany komunikatów z serwerem, w konstruktorze
        //podajemy ip serwera, z którym się łączymy oraz jego port
        MessageCommunicationClientClass messageClient =
                new MessageCommunicationClientClass("192.168.0.28",8888);
        mainMessageClient = messageClient;
        messageClient.startMsgClient();
        messageClient.sendMessage("CONNECT");


        String response = messageClient.receiveMessage();
        if(response.equals("OK")){
            new Thread(() -> {
                System.out.println("Serwer Voip started!!!!");
                voipConnection.receiveCall();
            }).start();

            voipConnection.captureAudio("192.168.0.28",9999);

            Platform.runLater(() -> {
                stageToClose.close();
                String selectedUser = contactsList.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("callWindow.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    System.out.println("Exception in handleConnection function");
                    System.out.println(e);
                }
                Stage stage = new Stage();
                stage.setTitle("Połączenie");
                Scene scene = new Scene(root);
                scene.getStylesheets().add("sample/style.css");
                stage.setScene(scene);

                CallWindowController controller = loader.getController();
                controller.setTargetUser(selectedUser);

                stage.setOnCloseRequest(event -> {
                    System.out.println("Zakończenie połączenia");

                    //Rozłączanie po kliknięciu krzyżyka oraz ustawienie flagi microphoneON na false wewnatrz funkcji
                    voipConnection.stopServer();
                    messageClient.sendMessage("DISCONNECT");
                    voipConnection.stopCapture();
                    messageClient.closeMsgClient();

                });
                callWindowStage = stage;
                stage.show();
            });


//            String selectedUser = contactsList.getSelectionModel().getSelectedItem();
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("callWindow.fxml"));
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.setTitle("Połączenie");
//            Scene scene = new Scene(root);
//            scene.getStylesheets().add("sample/style.css");
//            stage.setScene(scene);
//
//            CallWindowController controller = loader.getController();
//            controller.setTargetUser(selectedUser);
//
//            stage.setOnCloseRequest(event -> {
//                System.out.println("Zakończenie połączenia");
//
//                //Rozłączanie po kliknięciu krzyżyka oraz ustawienie flagi microphoneON na false wewnatrz funkcji
//                voipConnection.stopServer();
//                messageClient.sendMessage("DISCONNECT");
//                voipConnection.stopCapture();
//                messageClient.closeMsgClient();
//
//            });
//
//            stage.show();
        }
        else if (response.equals("REJECT")){
            Platform.runLater(() -> {
                stageToClose.close();
            });

            messageClient.closeMsgClient();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Niepowodzenie");
                alert.setTitle("Połączenie zerwane");
                alert.setContentText("Użytkownik odrzucił twoje połączenie");
                alert.showAndWait();
            });

        }

        //Koniec kodu do wrzucenia
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
