package edu.sustech.xiangqi.ui;
import java.util.List;


import edu.sustech.xiangqi.model.ChessBoardModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ArchiveManager extends JFrame{

    private List<ChessBoardModel> archives;
    private ArchivePanel archivePanel;
    private JScrollPane scrollPane;
    
    public ArchiveManager(List<ChessBoardModel> archives) {
        setTitle("中国象棋-本地存档");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        this.archives = archives;
        // 创建自定义列表面板
        archivePanel = new ArchivePanel(this.archives);
        
        // 将列表面板放入滚动窗格
        // 使用 BorderLayout，这样可以在底部留出固定高度的区域
        getContentPane().setLayout(new BorderLayout());

        scrollPane = new JScrollPane(archivePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 底部占位面板，用于插入其他组件，固定高度 50
        JPanel bottomPlaceholder = new JPanel();
        bottomPlaceholder.setPreferredSize(new Dimension(0, 50)); // 宽度由布局决定，高度固定为 50
        add(bottomPlaceholder, BorderLayout.SOUTH);
        
        add(scrollPane);

        archivePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }
    private void handleMouseClick(int x, int y){

        int Idx = y / archivePanel.getArchiveHeight();
        boolean haveChosen = (Idx==archivePanel.getSelectedIdx());
        if ((!haveChosen) && Idx >= 0 && Idx < archives.size()) {
            archivePanel.setSelectedIdx(Idx);
            repaint();
        }
        else if(haveChosen && Idx >= 0 && Idx < archives.size()){
            ChessBoardModel model = this.archives.get(Idx);
            ChessBoard chessBoard = new ChessBoard(model);
            archivePanel.setSelectedIdx(-1);
            setVisible(false);
            chessBoard.setVisible(true);
        }
    }
}

class ArchivePanel extends JPanel{

    private List<ChessBoardModel> archives;
    private int archiveHeight = 80;
    private int selectedIdx = -1;

    public ArchivePanel(List<ChessBoardModel> archives){
        this.archives = archives;
        setBackground(new Color(220, 179, 92));
        setPreferredSize(new Dimension(400, archives.size() * archiveHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Demo的GUI都是由Swing中基本的组件组成的，比如背景的格子是用许多个line组合起来实现的，棋子是先绘制一个circle再在上面绘制一个text实现的
        // 因此绘制GUI的过程中需要自己手动计算每个组件的位置（坐标）
        drawArchives(g2d);
    }

    private void drawArchives(Graphics2D g){
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));

        for(int archIdx=0;archIdx<archives.size();archIdx++){
            ChessBoardModel archNow = archives.get(archIdx);
            
            int y = archIdx*archiveHeight;
            if(archIdx == selectedIdx) g.setColor(new Color(200, 220, 255));
            else if(archIdx % 2 == 0) g.setColor(Color.WHITE);
            else g.setColor(new Color(245, 245, 245));
            
            g.fillRect(0, y, getWidth(), archiveHeight);
            
            // 绘制边框
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y, getWidth(), y);
            
            // 绘制标题
            g.setColor(Color.BLACK);
            g.setFont(new Font("SimHei", Font.BOLD, 20));
            g.drawString(archNow.getName(), 20, y + 30);
            
            // 绘制选中指示器
            if(archIdx == selectedIdx){
                g.setColor(new Color(0, 120, 215));
                g.fillRect(0, y, 5, archiveHeight);
            }
        }
        
        // 绘制底部边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, archives.size()*archiveHeight, getWidth(), archives.size() * archiveHeight);
    }

    public int getArchiveHeight(){
        return this.archiveHeight;
    }
    public int getSelectedIdx(){
        return this.selectedIdx;
    }
    public void setSelectedIdx(int selectedIdx){
        this.selectedIdx = selectedIdx;
    }
}
