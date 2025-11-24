package edu.sustech.xiangqi.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginPage extends JFrame{
    public LoginPage(){
        setTitle("中国象棋-登录");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);

        JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(10, 60);
        userNameTip.setSize(120,40);
        add(userNameTip);

        JTextField userName = new JTextField();
        userName.setLocation(60, 60);
        userName.setSize(100, 40);
        add(userName);

        JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(10, 110);
        passwordTip.setSize(120,40);
        add(passwordTip);

        JTextField password = new JTextField();
        password.setLocation(60, 110);
        password.setSize(100, 40);
        add(password);

        JButton LoginButton = new JButton("登录");
        LoginButton.setLocation(10, 160);
        LoginButton.setSize(100, 40);
        add(LoginButton);
        /* LoginButton.addActionListener(e1->{
            switchToBoards();
        }); */
    }
}
