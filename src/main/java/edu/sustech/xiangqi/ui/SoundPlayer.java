package edu.sustech.xiangqi.ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    // 音效文件路径
    private static final String SOUND_PATH = "src/main/java/edu/sustech/xiangqi/assets/sounds/SoundOfPlaceChess.wav";  // 移动音效

    // 播放移动音效
    public static void playMoveSound() {
        playSound();
    }

    // 核心播放方法
    private static void playSound() {
        try {
            // 加载音频文件
            File soundFile = new File(SoundPlayer.SOUND_PATH);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // 获取音频格式并打开剪辑
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);

            // 打开并播放
            clip.open(audioIn);
            clip.start();

            // 播放完毕后释放资源（通过监听器）
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // 音效文件不存在或格式错误时不报错（避免影响主程序）
            System.out.println("音效播放失败: " + e.getMessage());
        }
    }
}
