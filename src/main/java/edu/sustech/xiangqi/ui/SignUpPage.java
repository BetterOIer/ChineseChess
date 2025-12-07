package edu.sustech.xiangqi.ui;

import javax.swing.*;

public class SignUpPage extends JFrame{

    private JTextField userName;
    private JPasswordField password;
    private JButton signUp;
    private JLabel userNameOccupied;
    private JLabel userNameInvalid;
    private JLabel passwordInvalid;

    public SignUpPage(){
        setTitle("中国象棋-注册");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);

        JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(10, 60);
        userNameTip.setSize(120,40);
        add(userNameTip);

        userName = new JTextField();
        userName.setLocation(60, 60);
        userName.setSize(100, 40);
        add(userName);

        userNameOccupied = new JLabel("用户已存在！");
        userNameOccupied.setForeground(java.awt.Color.RED);
        userNameOccupied.setVisible(false);
        userNameOccupied.setLocation(180, 60);
        userNameOccupied.setSize(120,40);
        add(userNameOccupied);

        userNameInvalid = new JLabel("用户名为空！");
        userNameInvalid.setForeground(java.awt.Color.RED);
        userNameInvalid.setVisible(false);
        userNameInvalid.setLocation(180, 60);
        userNameInvalid.setSize(120,40);
        add(userNameInvalid);

        passwordInvalid = new JLabel("密码为空！");
        passwordInvalid.setForeground(java.awt.Color.RED);
        passwordInvalid.setVisible(false);
        passwordInvalid.setLocation(180, 110);
        passwordInvalid.setSize(120,40);
        add(passwordInvalid);

        JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(10, 110);
        passwordTip.setSize(120,40);
        add(passwordTip);

        password = new JPasswordField();
        password.setLocation(60, 110);
        password.setSize(100, 40);
        add(password);

        signUp = new JButton("注册");
        signUp.setLocation(10, 160);
        signUp.setSize(100, 40);
        add(signUp);
    }
    public JTextField getUserName(){
        return this.userName;
    }
    public JPasswordField getPassWord(){
        return this.password;
    }
    public JButton getSignUpButton(){
        return this.signUp;
    }
    public JLabel getUserNameOccupied(){
        return this.userNameOccupied;
    }
    public JLabel getUserNameInvalid(){
        return this.userNameInvalid;
    }
    public JLabel getPasswordInvalid(){
        return this.passwordInvalid;
    }
}
