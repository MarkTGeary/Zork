import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class SoundStuff {

    //Plays my short sound clips
    public static void playSound(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

//Loops Background Music and Pauses/Unpauses
class BackgroundSoundStuff implements Runnable {
    private String filePath;
    public static Clip clip;

    public BackgroundSoundStuff(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        playMusic(filePath);
    }

    public static void playMusic(String filePath) {
        try {
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pauseUnpauseMusic() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            } else {
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }
}
