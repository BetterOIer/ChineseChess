package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class JRoundTextField extends JTextField {
    private int radius = 15;

    public JRoundTextField() {
        super();
        init();
    }

    public JRoundTextField(String text) {
        super(text);
        init();
    }

    public JRoundTextField(int columns) {
        super(columns);
        init();
    }

    public JRoundTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    private void init() {
        setOpaque(false);
        // 浅灰色背景
        setBackground(new Color(230, 230, 230));
        // 无边框，但设置内边距防止文字贴边
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, radius, radius));

        g2.dispose();
        
        super.paintComponent(g);
    }
}
