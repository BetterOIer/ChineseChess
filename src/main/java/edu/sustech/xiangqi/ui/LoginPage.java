package edu.sustech.xiangqi.ui;

import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import edu.sustech.xiangqi.model.DBOperationUser;
import edu.sustech.xiangqi.model.User;

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

        JButton loginButton = new JButton("登录");
        loginButton.setLocation(10, 160);
        loginButton.setSize(100, 40);
        add(loginButton);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    User userTmp = DBOperationUser.getUserByName(userName.getText());
                    if(DBOperationUser.calHash(password.getText()).equals(userTmp.getPswordHash()))switchToConnection(userTmp);
                }catch(SQLException e2){
                    e2.printStackTrace();
                }
            }
        });

        JLabel signUpLink = new JLabel("<html><u>还没有账号？点击注册</u></html>");
        signUpLink.setLocation(130, 160);
        signUpLink.setSize(150, 40);
        add(signUpLink);
        signUpLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToSignUp();
            }
        });
    }
    private void switchToSignUp(){
        SignUpPage signUpPage = new SignUpPage();
        signUpPage.setVisible(true);
        signUpPage.getSignUpButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signUpPage.getUserNameOccupied().setVisible(false);
                signUpPage.getUserNameInvalid().setVisible(false);
                signUpPage.getPasswordInvalid().setVisible(false);
                try{
                    if(DBOperationUser.getUserByName(signUpPage.getUserName().getText())!=null){
                        signUpPage.getUserNameOccupied().setVisible(true);
                        signUpPage.getUserName().setText("");
                        signUpPage.getPassWord().setText("");
                    }else if(signUpPage.getUserName().getText().equals("")){
                        signUpPage.getUserNameInvalid().setVisible(true);
                    }else if(signUpPage.getPassWord().getText().equals("")){
                        signUpPage.getPasswordInvalid().setVisible(true);
                    }else{
                        DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(), signUpPage.getUserName().getText(), signUpPage.getPassWord().getText()));
                        signUpPage.dispose();
                    }
                }catch(SQLException e2){
                    e2.printStackTrace();
                }
            }
        });
    }
    private void switchToConnection(User user){
        Connection connection = new Connection(user);
        connection.setVisible(true);
        setVisible(false);
    }
}
