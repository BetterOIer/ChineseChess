package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.event.*;

public class ChessBoard extends JFrame {

    private ChessBoardModel model;

    private BackgroundPanel backgroundPanel;
    private JPanel mainPanel;
    private ChessBoardPanel chessBoardPanel;
    private PlayBackPanel playBackPanel;
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;

    private int windowWidth = (int)(Style.screenSize.width*0.7);
    private int windowHeight = (int)(Style.screenSize.height*0.7);
    private int windowHeightOffset = 37;

    public ChessBoard(ChessBoardModel model){

        //加载模型
        this.model=model;

        //设置窗口
        setTitle("中国象棋-"+model.getName());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
        setSize(windowWidth,windowHeight+windowHeightOffset);
        setLocationRelativeTo(null);

        // 创建背景面板并设置为主容器
        backgroundPanel = new BackgroundPanel(this);
        setContentPane(backgroundPanel);

        // 创建主面板
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);
        mainPanel.setBackground(Style.transprentColor);
        mainPanel.setSize(windowWidth,windowHeight);
        mainPanel.setLocation(0,0);

        // 创建顶部状态栏
        statusPanel = new StatusPanel(model,this);
        mainPanel.add(statusPanel);

        // 创建棋盘面板
        chessBoardPanel = new ChessBoardPanel(model,this);
        mainPanel.add(chessBoardPanel);

        // 创建左侧控制面板
        controlPanel = new ControlPanel(model, this);
        mainPanel.add(controlPanel);

        //创建右侧复盘面板
        playBackPanel = new PlayBackPanel(model, this);
        mainPanel.add(playBackPanel);

        //背景板加上主面板
        backgroundPanel.add(mainPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowWidth = backgroundPanel.getWidth();
                windowHeight = backgroundPanel.getHeight();

                mainPanel.setSize(windowWidth, windowHeight);

                if (statusPanel != null) statusPanel.resizeComponents();
                if (chessBoardPanel != null) chessBoardPanel.resizeComponents();
                if (controlPanel != null) controlPanel.resizeComponents();
                if (playBackPanel != null) playBackPanel.resizeComponents();
            }
        });
    }

    public BackgroundPanel getBackgroundPanel(){
        return this.backgroundPanel;
    }
    public void setBackgroundPanel(BackgroundPanel backgroundPanel){
        this.backgroundPanel=backgroundPanel;
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }
    public void setMainPanel(JPanel mainPanel){
        this.mainPanel = mainPanel;
    }

    public ChessBoardPanel getChessBoardPanel(){
        return chessBoardPanel;
    }
    public void setChessBoardPanel(ChessBoardPanel chessBoardPanel){
        this.chessBoardPanel = chessBoardPanel;
    }

    public PlayBackPanel getPlayBackPanel(){
        return playBackPanel;
    }
    public void setPlayBackPanel(PlayBackPanel playBackPanel){
        this.playBackPanel = playBackPanel;
    }

    public ControlPanel getControlPanel(){
        return controlPanel;
    }
    public void setControlPanel(ControlPanel controlPanel){
        this.controlPanel = controlPanel;
    }

    public StatusPanel getStatusPanel(){
        return statusPanel;
    }
    public void setStatusPanel(StatusPanel statusPanel){
        this.statusPanel = statusPanel;
    }

    public int getWindowWidth(){
        return windowWidth;
    }
    public void setWindowWidth(int windowWidth){
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight(){
        return windowHeight;
    }
    public void setWindowHeight(int windowHeight){
        this.windowHeight = windowHeight;
    }
}