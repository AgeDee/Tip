package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class loginController {

    private UserDAO userDAO = new UserDAO();

    @FXML private TextField userLogin;
    @FXML private PasswordField userPassword;

    @FXML
    public void OnLoginButtonClick() throws IOException {
        if(userDAO.findByUserLogin(userLogin.getText()) == null){
            System.out.println("Login failed");
        }
        else if (userDAO.findByUserLogin(userLogin.getText()).getPassword().equals(userPassword.getText())){
            System.out.println("Login Success");
            CurrentUser.setUserId(userLogin.getText());
            Stage stage = (Stage) userLogin.getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("sample/style.css");
            stage.setScene(scene);
            stage.show();

        } else{
            System.out.println("Login failed");
        }
    }










}
