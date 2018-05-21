package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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

    @FXML
    void initialize(){
        infoLabel.setText("");
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
            if (1 == 1) { //todo, sprawdzanie czy dodawany user znajduje się już na liście kontaktów
                System.out.println("Dodawanie do listy użytkowników");
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
        Stage stage = (Stage) searchText.getScene().getWindow();
        stage.close();
    }

    void addResultItem(String name){
        resultList.getItems().add(name);
    }

    void clearResultList(){
        resultList.getItems().clear();
    }
}
