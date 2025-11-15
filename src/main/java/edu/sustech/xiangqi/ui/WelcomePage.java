package edu.sustech.xiangqi.ui;

import javax.swing.*;

import edu.sustech.xiangqi.model.ChessBoardModel;

public class WelcomePage {

    private JFrame frameWelcome;
    private JButton loginButton;
    private JTextField userName, password;

    private void switchToBoards(){
        String userNameText = userName.getText();
        String passwordText = password.getText();
        if((!userNameText.equals(""))&& (!passwordText.equals(""))){
            frameWelcome.setVisible(false);

            JFrame frame = new JFrame("中国象棋");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel label = new JLabel("开始");
            label.setLocation(600, 100);
            label.setSize(100, 50);
            frame.add(label);
            JButton button = new JButton("start");
            button.setLocation(600, 200);
            button.setSize(100, 50);
            frame.add(button);

            ChessBoardModel model = new ChessBoardModel();
            ChessBoardPanel boardPanel = new ChessBoardPanel(model);

            frame.add(boardPanel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    public WelcomePage(){
        frameWelcome  = new JFrame("Welcome!");
        frameWelcome.setLayout(null);
        frameWelcome.setSize(300,300);
        frameWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel msgGreetings = new JLabel("欢迎来玩中国象棋！");
        msgGreetings.setLocation(10, 10);
        msgGreetings.setSize(120,40);
        frameWelcome.add(msgGreetings);

        JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(10, 60);
        userNameTip.setSize(120,40);
        frameWelcome.add(userNameTip);

        userName = new JTextField();
        userName.setLocation(60, 60);
        userName.setSize(100, 40);
        frameWelcome.add(userName);

        JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(10, 110);
        passwordTip.setSize(120,40);
        frameWelcome.add(passwordTip);

        password = new JTextField();
        password.setLocation(60, 110);
        password.setSize(100, 40);
        frameWelcome.add(password);

        loginButton = new JButton("登录");
        loginButton.setLocation(10, 160);
        loginButton.setSize(100, 40);
        frameWelcome.add(loginButton);
        loginButton.addActionListener(e1->{
            switchToBoards();
        });
    }

    public JFrame getWelcomeFrame(){
        return frameWelcome;
    }
    public void setVisible(boolean flag){
        frameWelcome.setVisible(flag);
    }
}
