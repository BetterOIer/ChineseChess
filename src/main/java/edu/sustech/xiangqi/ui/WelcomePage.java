package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import edu.sustech.xiangqi.model.ChessBoardModel;

public class WelcomePage extends JFrame{

    private void switchToLoginPage(){
            setVisible(false);
            ChessBoardModel model = new ChessBoardModel();
            ChessBoard chessBoard = new ChessBoard(model);
            chessBoard.setVisible(true);
    }

    private void switchToArchMgr(){
        List<ChessBoardModel> archives = new ArrayList<>();//TODO:以后是从数据库导入
        //Temp line below
        archives.add(new ChessBoardModel());
        archives.add(new ChessBoardModel("test"));
        //temp line above
        ArchiveManager archiveManager = new ArchiveManager(archives);
        setVisible(false);
        archiveManager.setVisible(true);
    }

    public WelcomePage(){
        setTitle("中国象棋");
        setLayout(null);
        setSize(768,768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(220, 179, 92));
        // 设置窗口可缩放的最小尺寸并在缩放时强制维持该最小值
        setMinimumSize(new Dimension(600, 768));

        JLabel msgGreetings = new JLabel("欢迎来玩中国象棋！");
        msgGreetings.setFont(new Font("SimHei", Font.BOLD, 36));
        int greetMsgLabelWidth = 400;
        int greetMsgLabelHeight = 60;
        int greetMsgLabelOriginX = (getWidth()-greetMsgLabelWidth)/2;
        int greetMsgLabelOriginY = (getHeight()-greetMsgLabelHeight)/2/4;
        msgGreetings.setHorizontalAlignment(SwingConstants.CENTER);
        msgGreetings.setVerticalAlignment(SwingConstants.CENTER);
        msgGreetings.setBounds(greetMsgLabelOriginX, greetMsgLabelOriginY, greetMsgLabelWidth, greetMsgLabelHeight);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = (getWidth()-msgGreetings.getWidth())/2;
                msgGreetings.setLocation(Math.max(0, x), msgGreetings.getY());
            }
        });
        add(msgGreetings);

        JButton boardOnlyButton = new JButton("仅棋盘");
        boardOnlyButton.setFont(new Font("SimHei", Font.BOLD, 20));
        int boardOnlyButtonWidth = 400;
        int boardOnlyButtonHeight = 60;
        int boardOnlyButtonOriginX = (getWidth()-boardOnlyButtonWidth)/2;
        int boardOnlyButtonOriginY = (getHeight()-boardOnlyButtonHeight)/2;
        boardOnlyButton.setBounds(boardOnlyButtonOriginX, boardOnlyButtonOriginY, boardOnlyButtonWidth, boardOnlyButtonHeight);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            int boardOnlyButtonNowX = (getWidth() - boardOnlyButton.getWidth()) / 2;
            boardOnlyButton.setLocation(Math.max(0, boardOnlyButtonNowX), boardOnlyButton.getY());
            }
        });
        add(boardOnlyButton);
        boardOnlyButton.addActionListener(e1->{
            switchToArchMgr();
        });

        JButton remoteGameButton = new JButton("联机游戏");
        remoteGameButton.setFont(new Font("SimHei", Font.BOLD, 20));
        int remoteGameButtonWidth = 400;
        int remoteGameButtonHeight = 60;
        int remoteGameButtonOriginX = (getWidth()-remoteGameButtonWidth)/2;
        int remoteGameButtonOriginY = boardOnlyButton.getY() + boardOnlyButton.getHeight() + 40;
        remoteGameButton.setBounds(remoteGameButtonOriginX, remoteGameButtonOriginY, remoteGameButtonWidth, remoteGameButtonHeight);
        // 保证按钮在窗口水平居中：监听窗口大小变化而不是按钮本身
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            int remoteGameButtonNowX = (getWidth() - remoteGameButton.getWidth()) / 2;
            remoteGameButton.setLocation(Math.max(0, remoteGameButtonNowX), remoteGameButton.getY());
            }
        });
        add(remoteGameButton);
        remoteGameButton.addActionListener(e1->{
            switchToLoginPage();
        });

        JButton withAIButton = new JButton("人机");
        withAIButton.setFont(new Font("SimHei", Font.BOLD, 20));
        int withAIButtonWidth = 400;
        int withAIButtonHeight = 60;
        int withAIButtonOriginX = (getWidth()-withAIButtonWidth)/2;
        int withAIButtonOriginY = remoteGameButton.getY() + remoteGameButton.getHeight() + 40;
        withAIButton.setBounds(withAIButtonOriginX, withAIButtonOriginY, withAIButtonWidth, withAIButtonHeight);
        // 保证按钮在窗口水平居中：监听窗口大小变化而不是按钮本身
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            int withAIButtonNowX = (getWidth() - withAIButton.getWidth()) / 2;
            withAIButton.setLocation(Math.max(0, withAIButtonNowX), withAIButton.getY());
            }
        });
        add(withAIButton);
        /* withAIButton.addActionListener(e1->{
            switchToLoginPage();
        }); */

        /* JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(10, 60);
        userNameTip.setSize(120,40);
        add(userNameTip);

        userName = new JTextField();
        userName.setLocation(60, 60);
        userName.setSize(100, 40);
        add(userName);

        JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(10, 110);
        passwordTip.setSize(120,40);
        add(passwordTip);

        password = new JTextField();
        password.setLocation(60, 110);
        password.setSize(100, 40);
        add(password);

        boardOnlyButton = new JButton("登录");
        boardOnlyButton.setLocation(10, 160);
        boardOnlyButton.setSize(100, 40);
        add(boardOnlyButton);
        boardOnlyButton.addActionListener(e1->{
            switchToBoards();
        }); */
    }
}
