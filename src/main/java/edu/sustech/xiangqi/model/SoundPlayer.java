package edu.sustech.xiangqi.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private static final String SOUND_PATH = "src/main/java/edu/sustech/xiangqi/assets/sounds/SoundOfPlaceChess.wav";  // 移动音效

    // 播放移动音效
    public static void playMoveSound() {
        playSound();
    }

    // 播放方法
    private static void playSound() {
        try {
            File soundFile = new File(SoundPlayer.SOUND_PATH);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);

            clip.open(audioIn);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("音效播放失败: " + e.getMessage());
        }
    }
}
