package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    @FXML
    void initialize(){
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
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
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

}
