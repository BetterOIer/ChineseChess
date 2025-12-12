package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class JRoundButton extends JButton {
    private int radius = 15;

    public JRoundButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setForeground(new Color(20,20,20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (getModel().isPressed()) {
            g2.setColor(new Color(200, 200, 200,200));
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(240, 240, 240, 200));
        } else {
            g2.setColor(new Color(255, 255, 255,0));
        }

        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, radius, radius));

        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(3f));
        g2.draw(new RoundRectangle2D.Float(2, 2, width - 4, height - 4, radius-2, radius-2));

        g2.dispose();
        
        super.paintComponent(g);
    }
}
