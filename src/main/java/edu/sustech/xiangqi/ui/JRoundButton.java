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
        setMargin(new Insets(5, 15, 5, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (getModel().isPressed()) {
            g2.setColor(new Color(200, 200, 200));
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(240, 240, 240));
        } else {
            g2.setColor(new Color(255, 255, 255));
        }

        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, radius, radius));

        g2.setColor(Color.GRAY);
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, radius, radius));

        g2.dispose();
        
        super.paintComponent(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        // 确保宽度至少适应文本，并增加一点额外的空间
        size.width = Math.max(size.width + 20, size.height * 2); 
        return size;
    }
}
