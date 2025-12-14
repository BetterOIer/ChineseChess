package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateTransparentButton {
    private JButton createTransparentButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // 完全透明背景，只响应点击，不显示任何内容
                // 因为图片上已经有文字，所以按钮不需要显示文字
                // 定义棕色线段的颜色和厚度
                final Color LINE_COLOR = new Color(185, 145, 110); // 咖啡色/棕色
                final int LINE_THICKNESS = 2; // 线段厚度
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 2. 绘制顶部棕色线段（y坐标为0，厚度LINE_THICKNESS）
                g2.setColor(LINE_COLOR);
                g2.setStroke(new BasicStroke(LINE_THICKNESS)); // 设置线段厚度
                // 线段起点(x1=0, y1=0)，终点(x2=width, y2=0)，覆盖按钮宽度
                g2.drawLine(0, 0, width, 0);

                // 3. 绘制底部棕色线段（y坐标为height-1，避免超出按钮边界）
                // 线段起点(x1=0, y1=height-1)，终点(x2=width, y2=height-1)
                g2.drawLine(0, height - 1, width, height - 1);

                // 绘制按钮文字（必须保留，否则文字不显示）
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

        // 添加鼠标悬停效果（可选，帮助用户发现按钮位置）
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
