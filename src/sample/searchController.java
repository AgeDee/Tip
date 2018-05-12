package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class searchController {

    @FXML
    ListView<String> resultList;

    @FXML
    void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList (
                "Testov", "Testowyużytkownik", "potest", "testtest");
        resultList.setItems(items);

    }
}
