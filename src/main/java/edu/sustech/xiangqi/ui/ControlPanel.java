package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ControlPanel extends JPanel{

    private ChessBoardModel model;
    private ChessBoard board;

    private WelcomePage welcomePage;

    private JButton reset, playBack, backToArchive, musicOn, surrender;


    public ControlPanel(ChessBoardModel model,ChessBoard board, WelcomePage welcomePage){

        this.model=model;
        this.board=board;
        this.welcomePage = welcomePage;

        setSize(board.getWindowWidth()/10, board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBackground(Style.defaultColor);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        Dimension buttonSize = new Dimension(board.getWindowWidth()/10, (int)(board.getWindowHeight()/20));

        reset = createTransparentButton("重置棋盘", 180, 40);
        /*reset.setPreferredSize(buttonSize);
        reset.setMaximumSize(buttonSize);
        reset.setMinimumSize(buttonSize);
        reset.setFont(new Font("隶书", Font.BOLD, 16));
        reset.setFocusPainted(false);
         */
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        reset.setSize(180, 40);
        add(reset);
        if(model.getAllSteps().isEmpty())reset.setVisible(false);

        playBack = new JRoundButton("复盘");
        playBack.setPreferredSize(buttonSize);
        playBack.setMaximumSize(buttonSize);
        playBack.setMinimumSize(buttonSize);
        playBack.setFont(new Font("隶书", Font.BOLD, 16));
        playBack.setFocusPainted(false);
        playBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))==0)playBack.setVisible(false);

        backToArchive = new JRoundButton("返回存档");
        backToArchive.setPreferredSize(buttonSize);
        backToArchive.setMaximumSize(buttonSize);
        backToArchive.setMinimumSize(buttonSize);
        backToArchive.setFont(new Font("隶书", Font.BOLD, 16));
        backToArchive.setFocusPainted(false);
        backToArchive.setAlignmentX(Component.CENTER_ALIGNMENT);

        musicOn = new JRoundButton("音乐");
        musicOn.setPreferredSize(buttonSize);
        musicOn.setMaximumSize(buttonSize);
        musicOn.setMinimumSize(buttonSize);
        musicOn.setFont(new Font("隶书", Font.BOLD, 16));
        musicOn.setFocusPainted(false);
        musicOn.setAlignmentX(Component.CENTER_ALIGNMENT);

        surrender = new JRoundButton("投降");
        surrender.setPreferredSize(buttonSize);
        surrender.setMaximumSize(buttonSize);
        surrender.setMinimumSize(buttonSize);
        surrender.setFont(new Font("隶书", Font.BOLD, 16));
        surrender.setFocusPainted(false);
        surrender.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))!=0)surrender.setVisible(false);

        // 添加按钮到控制面板
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/37.8)));
        add(backToArchive);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(musicOn);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(surrender);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(reset);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(playBack);
        add(Box.createVerticalGlue());

        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                model.resetBoard();
                board.getPlayBackPanel().setVisible(false);;
                updateControlPanel();
                board.getStatusPanel().updateDisplay();
                revalidate();repaint();
                board.repaint();
            }
        });

        playBack.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                handleClickOnPlayBack(model);
            }
        });

        backToArchive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                returnToArchiveManager();
            }
        });

        musicOn.addMouseListener(new MouseAdapter() {

        });

        surrender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSurrender(); // 调用投降处理方法
            }
        });
    }

    public void resizeComponents() {
        setSize(board.getWindowWidth()/10, board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        
        Dimension buttonSize = new Dimension(board.getWindowWidth()/10, (int)(board.getWindowHeight()/20));
        
        JButton[] buttons = {reset, playBack, backToArchive, musicOn, surrender};
        for (JButton btn : buttons) {
            if (btn != null) {
                btn.setPreferredSize(buttonSize);
                btn.setMaximumSize(buttonSize);
                btn.setMinimumSize(buttonSize);
            }
        }
        
        removeAll();
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/37.8)));
        add(backToArchive);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(musicOn);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(surrender);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(reset);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(playBack);
        add(Box.createVerticalGlue());
        
        revalidate();
        repaint();
    }

    public void updateControlPanel(){
        if((model.getType()&(1<<3))!=0 && (!model.getPlayBackOn())){
            reset.setVisible(true);
            playBack.setVisible(true);
            surrender.setVisible(false);
        }else if((model.getType()&(1<<3))!=0 && model.getPlayBackOn()){
            reset.setVisible(true);
            playBack.setVisible(false);
            surrender.setVisible(false);
        }else{
            if(model.getAllSteps().isEmpty())reset.setVisible(false);
            else reset.setVisible(true);
            playBack.setVisible(false);
            surrender.setVisible(true);
        }
        revalidate();
        repaint();
    }


    private void handleClickOnPlayBack(ChessBoardModel model){
        playBack.setVisible(false);
        model.setPlayBackOn(true);
        board.getPlayBackPanel().resetIdx();
        board.getPlayBackPanel().setVisible(true);
        board.getPlayBackPanel().repaint();
        revalidate();repaint();board.repaint();
        board.getPlayBackPanel().getContentPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnPlayBackPanel(e.getX(), e.getY());
            }
        });
    }
    
    private void handleMouseClickOnPlayBackPanel(int x, int y){
        model.setSelectedIdx(y/board.getPlayBackPanel().getStepHeight());
        replacePieces(model.getSelectedIdx());
        board.getPlayBackPanel().getContentPanel().repaint();
    }

    private void replacePieces(int stepIdx){
        model.tryPlayBack(stepIdx);
        board.getChessBoardPanel().repaint();
    }

    private void returnToArchiveManager(){
        try {
            // 获取当前用户的存档列表
            User currentUser = DBOperationUser.getUserInUse();
            List<ChessBoardModel> archives = DBOperationBoard.getBoardsByUser(currentUser);
            ArchiveManager archiveManager = new ArchiveManager(archives, welcomePage);
            archiveManager.setVisible(true);
            board.dispose(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSurrender() {
        // 1. 判定胜负（当前回合方投降，对方胜利）
        model.setType((model.getType()|8));
        System.out.println(model.getType());
        try{
            DBOperationBoard.updateBoardType(model.getId(), model.getType());
        }catch(SQLException e){
            e.printStackTrace();
        }
        board.getStatusPanel().updateDisplay();
        revalidate();repaint();
        board.getChessBoardPanel().repaint();
    }
    // 新增：投降后更新控制面板状态（隐藏不必要按钮）
    private void updateControlPanelAfterSurrender() {
        surrender.setVisible(false); // 隐藏投降按钮
        reset.setVisible(false); // 隐藏重置按钮
        playBack.setVisible(false); // 隐藏复盘按钮（如果需要可保留）
        // 只保留返回存档和音乐按钮
        backToArchive.setVisible(true);
        musicOn.setVisible(true);
        revalidate();
        repaint();
    }

    private JButton createTransparentButton(String text, int width, int height) {
        JButton button = new JButton(text) {
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
                g2.drawLine(0, 0, width + 1, 0);

                // 3. 绘制底部棕色线段（y坐标为height-1，避免超出按钮边界）
                // 线段起点(x1=0, y1=height-1)，终点(x2=width, y2=height-1)
                g2.drawLine(0, height - 1, width + 1, height - 1);

                // 绘制按钮文字（必须保留，否则文字不显示）
                g.setFont(new Font("隶书", Font.BOLD, 18));
                g.setColor(new Color(111, 78, 55));
                super.paintComponent(g);
            }
        };

        // 设置完全透明的按钮
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果（可选，帮助用户发现按钮位置）
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标悬停时显示半透明边框，提示按钮位置
                button.setBorder(BorderFactory.createLineBorder(
                        new Color(255, 255, 255, 100), 20
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
            }
        });
        return button;
    }

    public static JLabel createTextField(String text, int width, int height) {
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
