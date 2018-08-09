package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.UserDataHandler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LoginController {

    public static String DB_URL;
    //"jdbc:mysql://tipdb.czufm3rgmejc.eu-central-1.rds.amazonaws.com:3306/tip_db"

    //private UserDAO userDAO = new UserDAO();

    private UserDAO userDAO;

    public static String user_ip = "";

    @FXML private TextField userLogin;
    @FXML private PasswordField userPassword;
    @FXML private Label warningLabel;
    @FXML public TextField db_url;

    String loggedUserName;

    @FXML
    public void OnLoginButtonClick() throws IOException, NoSuchAlgorithmException {
        DB_URL = db_url.getText();
        userDAO = new UserDAO();
        if(userLogin.getLength() != 0) {
            if (userPassword.getLength() != 0) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(userPassword.getText().getBytes(StandardCharsets.UTF_8));
                String passwordHash = bytesToHex(hash);

                System.out.println("Hash hasła SHA-256: " + passwordHash);

                if (userDAO.findByUserLogin(userLogin.getText()) == null) {
                    System.out.println("Login Failed");
                    warningLabel.setTextFill(Color.RED);
                    warningLabel.setText("Login lub hasło są niepoprawne");
                }
                else if (userDAO.findByUserLogin(userLogin.getText()).getPassword().equals(passwordHash)) {
                    if(userDAO.findByUserLogin(userLogin.getText()).getUserIp() != null){
                        warningLabel.setTextFill(Color.RED);
                        warningLabel.setText("Użytkownik jest już zalogowany na innym urządzeniu");
                    }
                    else {
                        warningLabel.setText("");
                        loggedUserName = userLogin.getText();

                        try (final DatagramSocket socket = new DatagramSocket()) {
                            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                            user_ip = socket.getLocalAddress().getHostAddress();
                        } catch (Exception ex) {
                            System.out.println("Exception occured in getting local ip address of user");
                            System.out.println(ex);
                        }

                        userDAO.updateUserIpAddressById(userDAO.findByUserLogin(userLogin.getText()).getUserId(), user_ip);
                        System.out.println("Login Success");

                        CurrentUser.setUserLogin(userLogin.getText());
                        Stage stage = (Stage) userLogin.getScene().getWindow();
                        stage.close();

                        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add("sample/style.css");
                        stage.setScene(scene);

                        stage.setOnCloseRequest(event -> {
                            userDAO.updateUserIpAddressById(userDAO.findByUserLogin(loggedUserName).getUserId(), null);

                            CurrentUser.setUserLogin("");
                            try {
                                Parent rootForLoginParent = FXMLLoader.load(getClass().getResource("login.fxml"));
                                Scene afterCloseScene = new Scene(rootForLoginParent);
                                afterCloseScene.getStylesheets().add("sample/style.css");
                                Stage onCloseStage = new Stage();
                                onCloseStage.setScene(afterCloseScene);
                                onCloseStage.show();

                                MessageCommunicationClass.serverSocket.close();
                            } catch (IOException exception) {
                                System.out.println("Exception in LoginController:OnCloseRequest");
                                System.out.println(exception);
                            }

                        });

                        stage.show();
                    }

                } else {
                    System.out.println("Login Failed");
                    warningLabel.setTextFill(Color.RED);
                    warningLabel.setText("Login lub hasło są niepoprawne");
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
        DB_URL = db_url.getText();
        userDAO = new UserDAO();

        Stage stage = (Stage) userLogin.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);
        stage.setResizable(false);
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
