package edu.sustech.xiangqi.ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import edu.sustech.xiangqi.model.DBOperationUser;
import edu.sustech.xiangqi.model.User;

public class LoginPage extends JFrame{

    JButton loginButton,tourLoginButton;
    JRoundTextField userName;
    JRoundPasswordField password;
    JLabel namePwdWA;
    boolean force;
    private Image backgroundImage;


    int windowWidth = (int)(Style.screenSize.height*0.7);
    int windowHeight = (int)(Style.screenSize.height*0.7);

    public LoginPage(boolean force){
        System.out.println(windowHeight);
        setTitle("中国象棋-登录");
        setLayout(null);
        setSize(windowWidth,windowHeight);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 加载背景图片
        try {
            ImageIcon icon = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/LoginSignUpOutPage.png");
            backgroundImage = icon.getImage();

        }
        catch (Exception e) {
            System.out.println("背景图片加载失败: " + e.getMessage());
            backgroundImage = null;
            // 如果图片加载失败，使用默认背景色
            getContentPane().setBackground(new Color(240, 220, 200));
        }

        // 创建自定义背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // 绘制背景图片，填充整个面板
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);
        setResizable(false);

        CreateTransparentButton transparentButton = new CreateTransparentButton();
        CreateText text = new CreateText();

        JLabel userNameTip = text.createText("用户：", 100, 40);
        int userNameTipX = windowWidth / 4;
        int userNameTipY = windowHeight * 2 / 3 - 60;
        userNameTip.setBounds(userNameTipX, userNameTipY, 150, 45);
        userNameTip.setSize(150,40);
        add(userNameTip);

        userName = new JRoundTextField();
        userName.setLocation(userNameTipX + 120, userNameTipY);
        userName.setSize(160, 42);
        add(userName);

        JLabel passwordTip = text.createText("密码：", 100, 40);
        passwordTip.setBounds(userNameTipX, userNameTipY + 70, 150, 45);
        passwordTip.setSize(150,40);
        add(passwordTip);

        password = new JRoundPasswordField();
        password.setLocation(userNameTipX + 120, userNameTipY + 70);
        password.setSize(160, 42);
        add(password);

        loginButton = transparentButton.createTransparentButton("登录", 155, 40);
        loginButton.setLocation(userNameTipX - 30, userNameTipY + 140);
        loginButton.setSize(155, 40);
        add(loginButton);

        tourLoginButton = transparentButton.createTransparentButton("仅游客登录", 155, 40);
        tourLoginButton.setLocation(userNameTipX + 180, userNameTipY + 140);
        tourLoginButton.setSize(155, 40);
        tourLoginButton.setVisible(!force);
        add(tourLoginButton);

        namePwdWA = new JLabel("用户名或密码不正确！");
        namePwdWA.setForeground(java.awt.Color.RED);
        namePwdWA.setVisible(false);
        namePwdWA.setLocation(userNameTipX + 300, userNameTipY);
        namePwdWA.setSize(120,40);
        add(namePwdWA);

        JLabel signUpLink = new JLabel("<html><u>还没有账号？点击注册</u></html>");
        signUpLink.setLocation(userNameTipX - 10, userNameTipY + 180);
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
                    }else if(new String(signUpPage.getPassWord().getPassword()).equals("")){
                        signUpPage.getPasswordInvalid().setVisible(true);
                    }else{
                        DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(), signUpPage.getUserName().getText(), new String(signUpPage.getPassWord().getPassword()), 1));
                        signUpPage.dispose();
                    }
                }catch(SQLException e2){
                    e2.printStackTrace();
                }
            }
        });
    }

    public JButton getLoginButton(){
        return this.loginButton;
    }
    public JButton getTourLoginButton(){
        return this.tourLoginButton;
    }
    public JTextField getUserName(){
        return this.userName;
    }
    public JRoundPasswordField getPassword(){
        return this.password;
    }

    public JLabel getNamePwdWA(){
        return this.namePwdWA;
    }
}
