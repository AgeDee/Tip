package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class LogController {

    @FXML
    TableColumn userColumn;

    @FXML
    TableColumn descriptionColumn;

    @FXML
    TableColumn dateColumn;

    @FXML
    TableView tableView;

    @FXML
    Button closeButton;

    ObservableList<logRow> data = FXCollections.observableArrayList();

    ConnectionsLogDAO connectionsLogDAO = new ConnectionsLogDAO();
    UserDAO userDAO = new UserDAO();

    @FXML
    void initialize(){
        userColumn.setCellValueFactory(
                new PropertyValueFactory<logRow,String>("userLogin")
        );
        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<logRow,String>("description")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<logRow,String>("date")
        );

        tableView.setItems(data);

    }

    @FXML
    void closeAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void loadList(){
        List<ConnectionLog> connectionLogResultList = connectionsLogDAO.getAll();

        for(ConnectionLog c : connectionLogResultList){
            data.add(new logRow(userDAO.findByUserId(c.getUser2Id()).getLogin(), c.getDescription(), c.getDate()));
        }

        //todo tutaj pobieranie drugiego usera z wierszy gdzie znajduje się dany user (obojętnie czy jako user1 czy user2)
        //todo potem pobieranie loginu tego drugiego usera i dodawanie do listy 'data'
    }
}

