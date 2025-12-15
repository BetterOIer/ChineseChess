package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateTransparentButton extends JButton{
    public JButton createTransparentButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                final Color LINE_COLOR = new Color(185, 145, 110);
                final int LINE_THICKNESS = 2;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(LINE_COLOR);
                g2.setStroke(new BasicStroke(LINE_THICKNESS));
                g2.drawLine(0, 0, getWidth(), 0);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

                // 绘制按钮文字
                g.setFont(new Font("隶书", Font.BOLD, 22));
                g.setColor(new Color(111, 78, 55));
                super.paintComponent(g);
            }
        };

        // 设置完全透明的按钮
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标悬停时显示半透明边框，提示按钮位置
                button.setBorder(BorderFactory.createLineBorder(
                        new Color(255, 255, 255, 100), 20
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
            }
        });
        return button;
    }

}
