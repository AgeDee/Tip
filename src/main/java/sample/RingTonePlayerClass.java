package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class RingTonePlayerClass {
    private boolean stop = false;
    private String ringtone;
    private Media ringTone;
    public MediaPlayer ringTonePlayer;

    public RingTonePlayerClass(){
        String ringtone = "src\\main\\resources\\ring_tone.mp3";
        ringTone = new Media(new File(ringtone).toURI().toString());
        ringTonePlayer = new MediaPlayer(ringTone);
    }

    public void play(){
        while(!stop){
            //ringTonePlayer.
        }
        stop = false;
    }

    public void stop(){
        stop = true;
    }

}
