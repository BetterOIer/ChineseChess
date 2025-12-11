package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class PlayBackPanel extends JScrollPane {

    private ChessBoardModel model;
    private ChessBoard board;

    private int stepHeight = 40;

    private JPanel contentPanel;

    public PlayBackPanel(ChessBoardModel model, ChessBoard board) {
        this.model = model;
        this.board = board;

        setSize(board.getWindowWidth()/5, board.getWindowHeight()/13*12);
        setLocation(board.getWindowWidth()/5*4,board.getWindowHeight()/13);
        setOpaque(false);
        setBackground(Style.transprentColor);
        //TODO: 查验
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(111, 78, 55), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        setVisible(false);

        // 创建内部面板用于绘制内容
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawSteps(g2d, getWidth());
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(280, model.getTrueSteps().size() * stepHeight);
            }
        };
        
        contentPanel.setBackground(new Color(245, 222, 179));
        
        // 将内容面板设置为视口视图
        setViewportView(contentPanel);
        
        // 设置滚动条策略
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(16);
    }

    public void resizeComponents() {
        setSize(board.getWindowWidth()/5, board.getWindowHeight()/13*12);
        setLocation(board.getWindowWidth()/5*4,board.getWindowHeight()/13);
        revalidate();
        repaint();
    }

    private void drawSteps(Graphics2D g, int width) {
        g.setStroke(new BasicStroke(2));

        for (int stepIdx = 0; stepIdx < model.getTrueSteps().size(); stepIdx++) {
            Step stepNow = model.getTrueSteps().get(stepIdx);

            int y = stepIdx * stepHeight;
            if (stepIdx == model.getSelectedIdx()) g.setColor(new Color(200, 220, 255));
            else if (stepIdx % 2 == 0) g.setColor(Color.WHITE);
            else g.setColor(new Color(245, 245, 245));

            g.fillRect(0, y, width, stepHeight);

            // 绘制边框
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y, width, y);

            // 绘制标题
            g.setColor(Color.BLACK);
            g.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20));
            // 调整文字y坐标使其居中 (y是顶部，加上偏移量)
            g.drawString((stepIdx+1)+" "+stepNow.getStepNameInCh(), 20, y + 28);

            // 绘制选中指示器
            if (stepIdx ==  model.getSelectedIdx()) {
                g.setColor(new Color(0, 120, 215));
                g.fillRect(0, y, 5, stepHeight);
            }
        }

        // 绘制底部边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, model.getTrueSteps().size() * stepHeight, width, model.getTrueSteps().size() * stepHeight);
    }

    public JPanel getContentPanel(){
        return this.contentPanel;
    }
    public int getStepHeight(){
        return this.stepHeight;
    }

    public void resetIdx(){
        model.setSelectedIdx(model.getTrueSteps().size()-1);
    }
}