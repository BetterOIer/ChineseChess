package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class CreateText {
    public JLabel createText(String text, int width, int height) {
        JLabel label = new JLabel(text) {
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

                final Color textColor = Color.BLACK;
                g2.setFont(new Font("隶书", Font.PLAIN, 22));
                g2.setColor(textColor);
                // 获取字体度量信息，用于计算文字居中坐标
                FontMetrics fm = g2.getFontMetrics();
                // 计算文字水平居中：(标签宽度 - 文字宽度) / 2
                int textX = (width - fm.stringWidth(getText())) / 2;
                // 计算文字垂直居中：(标签高度 + 字体上升高度) / 2（上升高度是文字基线到顶部的距离）
                int textY = (height + fm.getAscent()) / 2 - fm.getDescent();

                // 绘制居中的文字（替代原有的super.paintComponent(g)）
                // 绘制按钮文字（必须保留，否则文字不显示）

                g2.drawString(getText(), textX, textY);

                g2.dispose(); // 释放资源
            }
        };
        return label;
    }

}


