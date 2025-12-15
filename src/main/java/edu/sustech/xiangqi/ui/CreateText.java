package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class CreateText {
    public JLabel createText(String text, int width, int height) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // 棕色线段的颜色和厚度
                final Color LINE_COLOR = new Color(185, 145, 110);
                final int LINE_THICKNESS = 2;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(LINE_COLOR);
                g2.setStroke(new BasicStroke(LINE_THICKNESS));
                g2.drawLine(0, 0, width, 0);
                g2.drawLine(0, height - 1, width, height - 1);

                final Color textColor = Color.BLACK;
                g2.setFont(new Font("隶书", Font.PLAIN, 22));
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (width - fm.stringWidth(getText())) / 2;
                int textY = (height + fm.getAscent()) / 2 - fm.getDescent();
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };
        return label;
    }
}


