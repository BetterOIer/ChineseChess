package edu.sustech.xiangqi.ui;
import java.util.List;


import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.DBOperationBoard;
import edu.sustech.xiangqi.model.DBOperationUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchiveManager extends JFrame{

    private List<ChessBoardModel> archives;
    private ArchivePanel archivePanel;
    private JRoundScrollPane scrollPane;
    private NoArchivePanel noArchivePanel;

    private boolean turn2Board=false;

    private void updateCenterPanel() {
        if (scrollPane != null) remove(scrollPane);
        if (noArchivePanel != null) remove(noArchivePanel);

        if (archives.isEmpty()) {
            add(noArchivePanel, BorderLayout.CENTER);
        } else {
            add(scrollPane, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }
    
    public ArchiveManager(List<ChessBoardModel> archives) {
        setTitle("中国象棋-本地棋局");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);

        this.turn2Board=false;
        this.archives = archives;
        // 创建自定义列表面板
        archivePanel = new ArchivePanel(this.archives);
        
        // 将列表面板放入滚动窗格
        getContentPane().setLayout(new BorderLayout());
        scrollPane = new JRoundScrollPane(archivePanel);
        scrollPane.setVerticalScrollBarPolicy(JRoundScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JRoundScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 底部面板
        bottomPlaceHolderPanel bottomplaceholder= new bottomPlaceHolderPanel();
        add(bottomplaceholder, BorderLayout.SOUTH);
        
        noArchivePanel = new NoArchivePanel();
        updateCenterPanel();

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

        bottomplaceholder.getBackButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnBackButton();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if(!turn2Board)new WelcomePage(false).setVisible(true);
            }
        });

        initKeyBindings();
    }

    private void initKeyBindings() {
        JRootPane rootPane = this.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(1);
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSelection(-1);
            }
        });

        actionMap.put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                archivePanel.setSelectedIdx(-1);
                archivePanel.repaint();
            }
        });

        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = archivePanel.getSelectedIdx();
                if (idx != -1 && idx < archives.size()) {
                    ChessBoardModel model = archives.get(idx);
                    ChessBoard chessBoard = new ChessBoard(model);
                    archivePanel.setSelectedIdx(-1);
                    chessBoard.setVisible(true);
                    turn2Board=true;
                    dispose();
                }
            }
        });

        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMouseClickOnDelButton();
            }
        });
    }

    private void moveSelection(int direction) {
        if (archives.isEmpty()) return;

        int currentIdx = archivePanel.getSelectedIdx();
        int newIdx;

        if (currentIdx == -1) {
            if (direction > 0) newIdx = 0;
            else newIdx = archives.size() - 1;
        } else {
            newIdx = currentIdx + direction;
        }

        if (newIdx >= 0 && newIdx < archives.size()) {
            archivePanel.setSelectedIdx(newIdx);
            scrollToVisible(newIdx);
            archivePanel.repaint();
        }
    }

    private void scrollToVisible(int idx) {
        if (scrollPane == null) return;
        int y = archivePanel.getItemY(idx);
        int h = archivePanel.getItemHeight(idx);
        archivePanel.scrollRectToVisible(new Rectangle(0, y, archivePanel.getWidth(), h));
    }

    private void handleMouseClickOnDelButton(){
        int Idx = archivePanel.getSelectedIdx();
        if(Idx!=-1){
            DelArchive delArchive = new DelArchive(Idx);

            JRootPane rootPane = delArchive.getRootPane();
            rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
            rootPane.getActionMap().put("confirm", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delArchive.getSubmitMod().doClick();
                }
            });
            rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
            rootPane.getActionMap().put("cancel", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    delArchive.getCancelMod().doClick();
                }
            });

            delArchive.setVisible(true);
            delArchive.getCancelMod().addActionListener(e1 -> {
                delArchive.dispose();
            });
            delArchive.getSubmitMod().addActionListener(e1 -> {
                try{
                    DBOperationBoard.deleteBoardById(DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse()).get(Idx).getId());
                    archives = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse());
                    archivePanel.setArchives(archives);
                    archivePanel.setSelectedIdx(-1);
                    updateCenterPanel();
                }catch(SQLException e){
                    e.printStackTrace();
                }finally{
                    delArchive.dispose();
                }
            });
            repaint();
        }
    }
    private void handleMouseClickOnNewButton(){
        try{
            //
            int Idx = DBOperationBoard.getBoardCount();
            /* System.out.println(Idx); */
            if(Idx!=-1){
                NewArchive newArchive = new NewArchive();

                JRootPane rootPane = newArchive.getRootPane();
                rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
                rootPane.getActionMap().put("confirm", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newArchive.getSubmitMod().doClick();
                    }
                });
                rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
                rootPane.getActionMap().put("cancel", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newArchive.getCancelMod().doClick();
                    }
                });

                newArchive.setVisible(true);
                newArchive.getCancelMod().addActionListener(e1 -> {
                    newArchive.dispose();
                });
                newArchive.getSubmitMod().addActionListener(e1 -> {
                    try{
                        DBOperationBoard.insertBoard(new ChessBoardModel(DBOperationBoard.getBoardCount(),newArchive.getBoardName().getText(), 1, newArchive.getDescription().getText(),DBOperationUser.getUserByName("Red"), DBOperationUser.getUserByName("Black"), DBOperationUser.getUserInUse() , newArchive.getWhoseTurn()));// 从 DB 重新读取所有存档并更新面板
                        archives = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse());
                        archivePanel.setArchives(archives);
                        updateCenterPanel();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }finally{
                        newArchive.dispose();
                    }
                });
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void handleMouseClickOnModButton(){
        int Idx = archivePanel.getSelectedIdx();
        if(Idx!=-1){
            ModifyArchive modifyArchive = new ModifyArchive(Idx);

            JRootPane rootPane = modifyArchive.getRootPane();
            rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
            rootPane.getActionMap().put("confirm", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    modifyArchive.getSubmitMod().doClick();
                }
            });
            rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
            rootPane.getActionMap().put("cancel", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    modifyArchive.getCancelMod().doClick();
                }
            });

            modifyArchive.setVisible(true);
            modifyArchive.getCancelMod().addActionListener(e1 -> {
                modifyArchive.dispose();
            });
            modifyArchive.getSubmitMod().addActionListener(e1 -> {
                try{
                    // 保存到数据库
                    DBOperationBoard.updateBoardName(DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse()).get(Idx).getId(), modifyArchive.getBoardName().getText());
                    DBOperationBoard.updateBoardDescription(DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse()).get(Idx).getId(), modifyArchive.getDescription().getText());
                    // 从 DB 重新读取所有存档并更新面板
                    archives = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse());
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
    }
    private void handleMouseClickOnBackButton(){
        dispose();
    }
    private void handleMouseClickOnPanel(int x, int y){

        int clickedIdx = -1;
        int currentY = 0;
        for (int i = archives.size() - 1; i >= 0; i--) {
            int h = archivePanel.getItemHeight(i);
            if (y >= currentY && y < currentY + h) {
                clickedIdx = i;
                break;
            }
            currentY += h;
        }
        
        int Idx = clickedIdx;
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
            chessBoard.setVisible(true);
            turn2Board=true;
            dispose();
        }
    }
}

class ArchivePanel extends JPanel{

    private List<ChessBoardModel> archives;
    private int archiveHeight = 80;
    private int selectedIdx = -1;
    private ChessBoardBirdView birdView;

    public ArchivePanel(List<ChessBoardModel> archives){
        this.archives = archives;
        setLayout(null);
        setBackground(new Color(245, 222, 179));
        updatePreferredSize();
    }

    public void setArchives(List<ChessBoardModel> archives){
        this.archives = archives;
        updatePreferredSize();
        revalidate();
        repaint();
    }

    private void updatePreferredSize() {
        int totalHeight = 0;
        for (int i = 0; i < archives.size(); i++) {
            totalHeight += getItemHeight(i);
        }
        setPreferredSize(new Dimension(400, Math.max(1, totalHeight)));
    }

    public int getItemHeight(int idx) {
        return (idx == selectedIdx) ? 380 : 80;
    }

    public int getItemY(int idx) {
        int y = 0;
        for (int i = archives.size() - 1; i > idx; i--) {
            y += getItemHeight(i);
        }
        return y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawArchives(g2d);
    }

    private void drawArchives(Graphics2D g){
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));

        for(int archIdx=0;archIdx<archives.size();archIdx++){
            ChessBoardModel archNow = archives.get(archIdx);
            
            int h = getItemHeight(archIdx);
            int y = getItemY(archIdx);
            
            if(archIdx == selectedIdx) {
                g.setColor(new Color(228,255,237));
                if (birdView != null) {
                    birdView.setLocation((getWidth()-300)/2, y+80);
                }
            }
            else if (archIdx % 2 == 0) g.setColor(Color.WHITE);
            else g.setColor(new Color(245, 245, 245));
            
            g.fillRect(0, y, getWidth(), h);
            
            // 绘制边框
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y, getWidth(), y);
            
            // 绘制标题
            g.setColor(Color.BLACK);
            g.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20));
            g.drawString(archNow.getName(), 20, y + 30);

            //绘制时间
            g.setColor(Color.GRAY);
            g.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 13));
            g.drawString(LocalDateTime.parse(archNow.getLastModTime(),DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 20, y + 60);

            int descWidth;
            //绘制描述
            if(archNow.getDescription()!=null){
                g.setColor(Color.BLACK);
                g.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 14));
                String description = archNow.getDescription();
                descWidth = g.getFontMetrics().stringWidth(description);
                g.drawString(description, getWidth() - descWidth - 20, y + 30);
            }


            //绘制描述
            g.setColor(Color.GRAY);
            g.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 13));
            String[] typeString = {"本地棋局", "网络对弈", "AI", "已结束"};
            int rightEdge=getWidth()-20;
            for(int i = 1,j=0;i<=8;i<<=1,j++){
                if((archNow.getType()&i)!=0){
                    descWidth = g.getFontMetrics().stringWidth(typeString[j]);
                    g.drawRoundRect(rightEdge - descWidth - 5, y + 45, descWidth + 10, 20, 10, 10);
                    g.drawString(typeString[j], rightEdge-descWidth, y + 60);
                    rightEdge-=(descWidth+20);
                }
            }

            // 绘制选中指示器
            if(archIdx == selectedIdx){
                g.setColor(new Color(104,184,142));
                g.fillRect(0, y, 5, h);
            }
            
            // 绘制底部边框
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y + h, getWidth(), y + h);
        }
    }

    public int getArchiveHeight(){
        return this.archiveHeight;
    }
    public int getSelectedIdx(){
        return this.selectedIdx;
    }
    public void setSelectedIdx(int selectedIdx){
        this.selectedIdx = selectedIdx;
        if (birdView != null) {
            remove(birdView);
            birdView = null;
        }
        if (selectedIdx != -1 && selectedIdx < archives.size()) {
            birdView = new ChessBoardBirdView(archives.get(selectedIdx), 400, 300);
            add(birdView);
        }
        updatePreferredSize();
        revalidate();
        repaint();
    }
}

class bottomPlaceHolderPanel extends JPanel{

    private JRoundButton modifyButton;
    private JRoundButton newButton;
    private JRoundButton delButton;
    private JRoundButton backButton;

    public bottomPlaceHolderPanel(){

        setPreferredSize(new Dimension(0, 60)); // 宽度由布局决定，高度固定为 60
        setLayout(null);

        newButton = new JRoundButton("新建");
        newButton.setSize(60, 30);
        newButton.setFont(new Font("隶书", Font.PLAIN, 20));
        /* newButton.setFont(new Font("隶书", Font.PLAIN, 20)); */
        add(newButton);

        modifyButton = new JRoundButton("修改");
        modifyButton.setSize(60, 30);
        modifyButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(modifyButton);

        delButton = new JRoundButton("删除");
        delButton.setSize(60, 30);
        delButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(delButton);

        backButton = new JRoundButton("返回");
        backButton.setSize(60, 30);
        backButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(backButton);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int w = getWidth();
        int btnW = 60;
        int gap = 5;
        int marginR = 15;
        int y = 15;

        if(delButton != null) delButton.setLocation(w - marginR - btnW, y);
        if(modifyButton != null) modifyButton.setLocation(w - marginR - 2*btnW - gap, y);
        if(newButton != null) newButton.setLocation(w - marginR - 3*btnW - 2*gap, y);
        if(backButton != null) backButton.setLocation(w - marginR - 4*btnW - 3*gap, y);
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
    public JButton getBackButton() { return backButton; }
}

class NoArchivePanel extends JPanel {
    public NoArchivePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel mainLabel = new JLabel("此地无棋三百局");
        mainLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 42));
        JLabel subLabel = new JLabel("点击右下角“新建”创建棋盘");
        subLabel.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 18));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(mainLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(subLabel, gbc);
    }
}