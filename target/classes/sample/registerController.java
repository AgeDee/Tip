package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterController {

    @FXML
    TextField loginField;

    @FXML
    TextField emailField;

    @FXML
    PasswordField password1Field;

    @FXML
    PasswordField password2Field;

    @FXML
    CheckBox termsCheckBox;

    @FXML
    Label infoLabel;

    @FXML
    Hyperlink termsHyperLink;

    private UserDAO userDAO = new UserDAO();

    @FXML
    void initialize() {
    }

    @FXML
    public void registerAction() throws NoSuchAlgorithmException, IOException {
        infoLabel.setTextFill(Color.BLACK);
        infoLabel.setText("Podaj swoje dane:");

        if (termsCheckBox.isSelected()) {
            String login = loginField.getText();
            String email = emailField.getText();
            String password1 = password1Field.getText();
            String password2 = password2Field.getText();

            if (!login.isEmpty()) {
                if (userDAO.findByUserLogin(login) == null) {
                    if (!email.isEmpty() && email.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
                        if (userDAO.findByUserEmail(email) == null) {
                            if (!password1.isEmpty() && !password2.isEmpty()) {
                                if (password1.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
                                    //tutaj rejestracja
                                    infoLabel.setTextFill(Color.GREEN);
                                    infoLabel.setText("Dane poprawne.");

                                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                    byte[] hash = digest.digest(password1.getBytes(StandardCharsets.UTF_8));
                                    String passwordHash = bytesToHex(hash);

                                    User newUser = new User(login, passwordHash, email);
                                    userDAO.create(newUser);

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Rejestracja");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Rejestracja przebiegła pomyślnie. Możesz się teraz zalogować.");
                                    alert.showAndWait();

                                    Stage stage = (Stage) loginField.getScene().getWindow();
                                    stage.close();

                                    Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("sample/style.css");
                                    stage.setScene(scene);
                                    stage.show();
                                }
                                else{
                                    infoLabel.setTextFill(Color.RED);
                                    infoLabel.setText("Hasło musi mieć min. 8 znaków i składać się z:\n" +
                                            "przynajmniej jednej małej litery, dużej litery,\n" +
                                            "liczby i znaku specjalnego");
                                    infoLabel.setWrapText(true);
                                    infoLabel.setTextAlignment(TextAlignment.JUSTIFY);
                                }
                            } else {
                                infoLabel.setTextFill(Color.RED);
                                infoLabel.setText("Hasła nie są takie same!");
                            }
                            if (password1.equals(password2)) {
                            } else {
                                infoLabel.setTextFill(Color.RED);
                                infoLabel.setText("Wpisz swoje hasło w oba okna!");
                            }
                        } else {
                            infoLabel.setTextFill(Color.RED);
                            infoLabel.setText("Ten email jest już w użyciu!");
                        }
                    } else {
                        infoLabel.setTextFill(Color.RED);
                        infoLabel.setText("Musisz podać poprawny adres email!");
                    }
                } else {
                    infoLabel.setTextFill(Color.RED);
                    infoLabel.setText("Taki użytkownik już istnieje");
                }
            } else {
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Musisz podać swój login!");
            }
        } else {
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText("Musisz zaakceptować regulamin!");
        }

    }

    @FXML
    void termsAction() throws IOException {
        System.out.println("Regulamin");

        Parent root = FXMLLoader.load(getClass().getResource("terms.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Regulamin");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void backAction() throws IOException {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("sample/style.css");
        stage.setScene(scene);
        stage.show();
    }


    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
