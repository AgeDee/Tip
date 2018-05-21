package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginController {

    private UserDAO userDAO = new UserDAO();

    @FXML private TextField userLogin;
    @FXML private PasswordField userPassword;

    @FXML
    public void OnLoginButtonClick() throws IOException, NoSuchAlgorithmException {
        if(userLogin.getLength() != 0) {
            if (userPassword.getLength() != 0) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(userPassword.getText().getBytes(StandardCharsets.UTF_8));
                String passwordHash = bytesToHex(hash);

                System.out.println("Hash hasła SHA-256: " + passwordHash);

                if (userDAO.findByUserLogin(userLogin.getText()) == null) {
                    System.out.println("Login Failed");
                }
                //else if (userDAO.findByUserLogin(userLogin.getText()).getPassword().equals(userPassword.getText())){
                else if (userDAO.findByUserLogin(userLogin.getText()).getPassword().equals(passwordHash)) {
                    System.out.println("Login Success");
                    CurrentUser.setUserId(userLogin.getText());
                    Stage stage = (Stage) userLogin.getScene().getWindow();
                    stage.close();

                    Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add("sample/style.css");
                    stage.setScene(scene);
                    stage.show();

                } else {
                    System.out.println("Login Failed");
                }
            } else {
                System.out.println("Podaj hasło!");
            }
        }else {
            System.out.println("Podaj login!");
        }
    }

    @FXML
    void OnRegisterButtonClick() throws IOException {
        Stage stage = (Stage) userLogin.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);
        stage.show();

    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }






}
