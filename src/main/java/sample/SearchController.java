package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class SearchController {

    @FXML
    ListView<String> resultList;

    @FXML
    Button searchButton;

    @FXML
    Button addSelectedButton;

    @FXML
    Button cancelButton;

    @FXML
    TextField searchText;

    @FXML
    Label infoLabel;

    private UserDAO userDAO = new UserDAO();
    private ContactDAO contactDAO = new ContactDAO();

    Stage stage;

    @FXML
    void initialize(){
        infoLabel.setText("");

        cancelButton.setOnAction(event -> {
            stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        });
    }

    @FXML
    void searchAction(){
        clearResultList();

        List<User> searchResult = userDAO.searchByUserLogin(searchText.getText());

        for(User u : searchResult){
            addResultItem(u.getLogin());
        }

    }

    @FXML
    void addSelectedAction(){
        if(resultList.getSelectionModel().getSelectedIndex() != -1) {
            int userId = userDAO.findByUserLogin(CurrentUser.getUserLogin()).getUserId();
            int selectedUserId = userDAO.findByUserLogin(resultList.getSelectionModel().getSelectedItem()).getUserId();
            List<Contact> contactListResult = contactDAO.findByUserId(userId);
            boolean isInList = false; //czy wybrany user znajduje sie juz na liście - flaga

            for(Contact c : contactListResult){
                if(c.getContact_id() == selectedUserId){
                    isInList = true;
                    break;
                }
            }


            if (!isInList) {
                System.out.println("Dodawanie do listy użytkowników");

                Contact newContact = new Contact(userId, selectedUserId); //tworzenie kontaktu
                contactDAO.create(newContact); //dodanie do bazy

                infoLabel.setTextFill(Color.GREEN);
                infoLabel.setText("Dodano do listy");
            } else {
                System.out.println("Wybrany użytkownik znajduje się już na liście!");
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Użytkownik już istnieje!");
            }
        }else{
            System.out.println("Nie wybrano elementu!");
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText("Nic nie wybrano!");
        }
    }

    @FXML
    void cancelAction(){
        //stage.close();
        //zastąpiono eventem w inicie
    }

    void addResultItem(String name){
        resultList.getItems().add(name);
    }

    void clearResultList(){
        resultList.getItems().clear();
    }

    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }
}
