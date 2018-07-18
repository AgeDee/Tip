package sample;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class CallToUserController {

    @FXML private Text informationalText;
    @FXML private ImageView telephoneImage;


    @FXML
    void initialize(){
        //telephoneImage.setImage(new Image("/telephone.png"));
    }

    public void setInfomrationalText(String userName){
        informationalText.setText("Dzwonię do " + userName + " ...");
    }





}
