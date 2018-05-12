package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class searchController {

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
    void initialize(){
    }

    @FXML
    void searchAction(){
        clearResultList();
        addResultItem("Testov");
        addResultItem("Testowyużytkownik");
        addResultItem("potest");
        addResultItem("testtest");
    }

    @FXML
    void addSelectedAction(){
        clearResultList();
    }

    @FXML
    void cancelAction(){

    }

    void addResultItem(String name){
        resultList.getItems().add(name);
    }

    void clearResultList(){
        resultList.getItems().clear();
    }
}
