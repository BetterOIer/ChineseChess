package edu.sustech.xiangqi.ui;
import java.util.List;


import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.DBOperationBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

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

        // 底部占位面板，用于插入其他组件，固定高度 60
        bottomPlaceHolderPanel bottomplaceholder= new bottomPlaceHolderPanel();
        add(bottomplaceholder, BorderLayout.SOUTH);
        
        add(scrollPane);

        archivePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnPanel(e.getX(), e.getY());
            }
        });

        bottomplaceholder.getModButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnModButton();
            }
        });

        bottomplaceholder.getNewButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnNewButton();
            }
        });

        bottomplaceholder.getDelButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnDelButton();
            }
        });
    }

    private void handleMouseClickOnDelButton(){
        int Idx = archivePanel.getSelectedIdx();
        if(Idx!=-1){
            try {
                DBOperationBoard.deleteBoardById(Idx);
                Idx=0;
                archives = DBOperationBoard.getAllBoards();
                archivePanel.setArchives(archives);
                archivePanel.revalidate();
                archivePanel.repaint();
                if (scrollPane != null) {
                    scrollPane.revalidate();
                }
                // 整体重绘窗口
                repaint();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

    private void handleMouseClickOnNewButton(){//其实这一块还有点问题...
        try{
            DBOperationBoard.insertBoard(new ChessBoardModel(DBOperationBoard.getBoardCount(), 1));
            int Idx = DBOperationBoard.getBoardCount()-1;
            /* System.out.println(Idx); */
            if(Idx!=-1){
                ModifyArchive modifyArchive = new ModifyArchive(Idx);
                modifyArchive.setVisible(true);
                modifyArchive.getCancelMod().addActionListener(e1 -> {
                    modifyArchive.dispose();
                });
                modifyArchive.getSubmitMod().addActionListener(e1 -> {
                    try{
                        // 保存到数据库
                        DBOperationBoard.updateBoardName(Idx, modifyArchive.getBoardName().getText());
                        DBOperationBoard.updateBoardDescription(Idx, modifyArchive.getDescription().getText());
                        // 从 DB 重新读取所有存档并更新面板
                        archives = DBOperationBoard.getAllBoards();
                        archivePanel.setArchives(archives);
                        archivePanel.revalidate();
                        archivePanel.repaint();
                        if (scrollPane != null) {
                            scrollPane.revalidate();
                        }
                        // 整体重绘窗口
                        repaint();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }finally{
                        modifyArchive.dispose();
                    }
                });
            }
            repaint();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void handleMouseClickOnModButton(){
        int Idx = archivePanel.getSelectedIdx();
        if(Idx!=-1){
            ModifyArchive modifyArchive = new ModifyArchive(Idx);
            modifyArchive.setVisible(true);
            modifyArchive.getCancelMod().addActionListener(e1 -> {
                modifyArchive.dispose();
            });
            modifyArchive.getSubmitMod().addActionListener(e1 -> {
                try{
                    // 保存到数据库
                    DBOperationBoard.updateBoardName(Idx, modifyArchive.getBoardName().getText());
                    DBOperationBoard.updateBoardDescription(Idx, modifyArchive.getDescription().getText());
                    // 从 DB 重新读取所有存档并更新面板
                    archives = DBOperationBoard.getAllBoards();
                    archivePanel.setArchives(archives);
                    archivePanel.revalidate();
                    archivePanel.repaint();
                    if (scrollPane != null) {
                        scrollPane.revalidate();
                    }
                    // 整体重绘窗口
                    repaint();
                }catch(SQLException e){
                    e.printStackTrace();
                }finally{
                    modifyArchive.dispose();
                }
            });
        }
        repaint();
    }
    private void handleMouseClickOnPanel(int x, int y){

        int Idx = y / archivePanel.getArchiveHeight();
        /* System.out.println(Idx); */
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

    public void setArchives(List<ChessBoardModel> archives){
        this.archives = archives;
        setPreferredSize(new Dimension(400, Math.max(1, archives.size()) * archiveHeight));
        revalidate();
        repaint();
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

            //绘制时间
            g.setColor(Color.GRAY);
            g.setFont(new Font("SimHei", Font.PLAIN, 13));
            g.drawString(archNow.getLastModTime(), 20, y + 60);
            

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

class bottomPlaceHolderPanel extends JPanel{

    private JButton modifyButton;
    private JButton newButton;
    private JButton delButton;

    public bottomPlaceHolderPanel(){

        setPreferredSize(new Dimension(0, 60)); // 宽度由布局决定，高度固定为 60
        setLayout(null);

        newButton = new JButton("新建");
        newButton.setSize(60, 30);
        newButton.setLocation(1161,15);
        add(newButton);

        modifyButton = new JButton("修改");
        modifyButton.setSize(60, 30);
        modifyButton.setLocation(1226,15);
        add(modifyButton);

        delButton = new JButton("删除");
        delButton.setSize(60, 30);
        delButton.setLocation(1291,15);
        add(delButton);
    }

    public JButton getModButton(){
        return modifyButton;
    }
    public JButton getNewButton(){
        return newButton;
    }
    public JButton getDelButton(){
        return delButton;
    }
}