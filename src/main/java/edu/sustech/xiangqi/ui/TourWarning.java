package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

import java.awt.Font;

public class TourWarning extends JFrame {

    JRoundButton submitAcknow;
    JRoundButton cancelAcknow;
    private Image backgroundImage;

    public TourWarning(){
        setTitle("提示");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            ImageIcon icon = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/TourWarning.JPG");
            backgroundImage = icon.getImage();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "背景图片加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            // 如果图片加载失败，使用默认大小
            setSize(768, 768);
            getContentPane().setBackground(new Color(220, 179, 92));
        }
        setResizable(false);

        // 创建自定义背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // 绘制背景图片，填充整个面板
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        JLabel delConfirm = new JLabel("使用游客模式登录，你只能开启单人模式！");
        delConfirm.setLocation(10, 40);
        delConfirm.setSize(400,30);
        delConfirm.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(delConfirm);


        cancelAcknow = new JRoundButton("去登录");
        cancelAcknow.setLocation(10, 90);
        cancelAcknow.setSize(80,30);
        cancelAcknow.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelAcknow);
        

        submitAcknow = new JRoundButton("了解");
        submitAcknow.setLocation(110, 90);
        submitAcknow.setSize(80, 30);
        submitAcknow.setFont(new Font("隶书", Font.PLAIN, 20));
        add(submitAcknow);
    }
    public JButton getSubmitAcknow(){
        return submitAcknow;
    }
    public JButton getCancelAcknow(){
        return cancelAcknow;
    }
}
