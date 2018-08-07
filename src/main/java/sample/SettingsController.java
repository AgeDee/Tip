package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {

    @FXML
    PasswordField pass1Field;

    @FXML
    PasswordField pass2Field;

    @FXML
    Label infoLabel;

    @FXML
    WebView avatarView;

    UserDAO userDAO = new UserDAO();

    String userLogin = CurrentUser.getUserLogin();

    @FXML
    void initialize() {
        setAvatar(AvatarManager.downloadAvatar(userLogin));

    }

    @FXML
    void selectAvatarAction() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj avatar...");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Zdjęcie (*.jpg;*.png)", "*.jpg;*png");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(new Stage());

        List<String> extensions = new ArrayList<>();

        extensions.add("png");
        extensions.add("jpg");
        extensions.add("jpeg");
        extensions.add("bmp");
        extensions.add("gif");

        if(file != null) {
            //sprawdzanie czy jpg
            String parts[] = file.getPath().toLowerCase().split("\\.");
            String extension = null;
            if(parts.length > 1) {
                extension = parts[parts.length - 1];
            }
            if(extension != null) {
                if(extensions.contains(extension) && extension.length() <= 4){
                    AvatarManager.uploadAvatar(file, userLogin);
                    setAvatar(AvatarManager.downloadAvatar(userLogin));
                    System.out.println("Ustawiono nowy avatar");
                }else {
                    System.out.println("Nieobsługiwane rozszerzenie pliku!");
                }
            }else{
                System.out.println("Niepoprawna ścieżka pliku!");
            }
        }else {
            System.out.println("Nie wczytano pliku");
        }
    }

    @FXML
    void changePassActon() throws NoSuchAlgorithmException {
        if(pass1Field.getLength() != 0 && pass2Field.getLength() != 0){
            if(pass1Field.getText().equals(pass2Field.getText())){
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(pass1Field.getText().getBytes(StandardCharsets.UTF_8));
                String passwordHash = bytesToHex(hash);

                User userNewPass = userDAO.findByUserLogin(userLogin);
                userNewPass.setPassword(passwordHash);

                userDAO.updateUserById(userNewPass.getUserId(), userNewPass.getLogin(), userNewPass.getPassword(), userNewPass.getEmail());
                System.out.println("Hasło zostało zmienione");

                //todo zmiana hasła
            }else{
                System.out.println("Podane hasła nie sa takie same!");
                //todo infolabel
            }
        }else{
            System.out.println("Proszę podać oba hasła.");
            //todo infolabel
        }
    }

    void setAvatar(String url){
        WebEngine webEngine = avatarView.getEngine();
        String avatarContent = "<img style=\"width: 133;height: 133px;\" src=\" " + url + " \"></img> ";
        webEngine.loadContent(avatarContent);
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
