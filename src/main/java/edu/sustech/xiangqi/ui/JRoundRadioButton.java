package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class JRoundRadioButton extends JRadioButton {

    public JRoundRadioButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 2));
        setForeground(new Color(20,20,20));
        
        // Hide default icons to look like a button
        setIcon(new EmptyIcon());
        setSelectedIcon(new EmptyIcon());
        setPressedIcon(new EmptyIcon());
        setRolloverIcon(new EmptyIcon());
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setIconTextGap(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (isSelected()) {
            g2.setColor(new Color(104,184,142));
            int h = height / 5;
            g2.fillRoundRect(0, height - h, width, h,5,5);
        }

        g2.dispose();
        
        super.paintComponent(g);
    }
    
    private static class EmptyIcon implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {}
        @Override
        public int getIconWidth() { return 0; }
        @Override
        public int getIconHeight() { return 0; }
    }
}
