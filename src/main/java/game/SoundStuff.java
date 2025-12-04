package game;

import java.io.BufferedInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundStuff {

    // Simple one-shot sound
    public static void playSound(String resourcePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(
                            SoundStuff.class.getResourceAsStream(resourcePath)
                    )
            );

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Loops background music and allows pause/unpause
class BackgroundSoundStuff implements Runnable {

    private String resourcePath;
    private static Clip clip;

    public BackgroundSoundStuff(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public void run() {
        playMusic(resourcePath);
    }

    public static void playMusic(String resourcePath) {
        try {
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(
                            BackgroundSoundStuff.class.getResourceAsStream(resourcePath)
                    )
            );

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
