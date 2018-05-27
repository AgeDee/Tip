package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class BlockedUsersController {

    @FXML
    Button closeButton;

    @FXML
    ListView<String> blockedList;

    BlockedUserDAO blockedUserDAO = new BlockedUserDAO();
    UserDAO userDAO = new UserDAO();

    String userLogin = userDAO.findByUserLogin(CurrentUser.getUserLogin()).toString();
    int userId = userDAO.findByUserLogin(CurrentUser.getUserLogin()).getUserId();
    private Stage stage;

    @FXML
    void initialize(){

        closeButton.setOnAction(event -> {
            stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        });

        reloadList();
    }

    @FXML
    void deleteAction(){
        if (blockedList.getSelectionModel().getSelectedIndex() != -1) {
            String selectedUserLogin = blockedList.getSelectionModel().getSelectedItem();
            int selectedUserId = userDAO.findByUserLogin(selectedUserLogin).getUserId();
            List<BlockedUser> blockedUsersResult = blockedUserDAO.getByUserId(userId);

            for(BlockedUser b : blockedUsersResult){
                if(b.getBlockedId() == selectedUserId){
                    blockedUserDAO.deleteById(b.getId());
                    System.out.println("Usunięto użytkownika z listy zablokowanych.");
                    reloadList();
                }
            }

        } else {
            System.out.println("Nie wybrano elementu!");
        }
    }

    @FXML
    void closeAction(){
//        Stage stage = (Stage) closeButton.getScene().getWindow();
//        stage.close();
        //zastąpiono w inicie
    }

    void addResultItem(String name){
        blockedList.getItems().add(name);
    }

    void clearResultList(){
        blockedList.getItems().clear();
    }

    void reloadList(){
        clearResultList();
        List<BlockedUser> blockedUsersResult = blockedUserDAO.getByUserId(userId);

        for(BlockedUser b : blockedUsersResult){
            addResultItem(userDAO.findByUserId(b.getBlockedId()).getLogin());
        }
    }

    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }
}
