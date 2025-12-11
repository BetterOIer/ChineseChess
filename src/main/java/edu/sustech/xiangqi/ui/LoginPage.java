package edu.sustech.xiangqi.ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import edu.sustech.xiangqi.model.DBOperationUser;
import edu.sustech.xiangqi.model.User;

public class LoginPage extends JFrame{

    JRoundButton loginButton,tourLoginButton;
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
            ImageIcon icon = new ImageIcon("src/main/java/edu/sustech/iangqi/assets/images/LoginPage.png");
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

        /*JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(145, 360);
        userNameTip.setSize(100,40);
        add(userNameTip);

         */
        userName = new JRoundTextField();
        userName.setSize((int)(windowWidth/4.5), (int)(windowHeight/15.5));
        userName.setLocation((int)(windowWidth/2), (int)(windowHeight/5*3));
        add(userName);

        /*JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(150, 430);
        passwordTip.setSize(120,40);
        add(passwordTip);

         */

        password = new JRoundPasswordField();
        password.setSize((int)(windowWidth/4.5), (int)(windowHeight/15.5));
        password.setLocation((int)(windowWidth/2), (int)(windowHeight/5*3.5));
        add(password);

        loginButton = new JRoundButton("登录");
        loginButton.setLocation((int)(windowHeight/1.89), (int)(windowHeight/5*4));
        loginButton.setSize((int)(windowWidth/7.3), (int)(windowWidth/18));
        add(loginButton);

        tourLoginButton = new JRoundButton("仅游客登录");
        tourLoginButton.setLocation((int)(windowHeight/4.725),(int)(windowHeight/5*4));
        tourLoginButton.setSize((int)(windowWidth/7.3), (int)(windowWidth/18));
        tourLoginButton.setVisible(!force);
        add(tourLoginButton);

        namePwdWA = new JLabel("用户名或密码不正确！");
        namePwdWA.setForeground(java.awt.Color.RED);
        namePwdWA.setVisible(false);
        namePwdWA.setLocation(180, 60);
        namePwdWA.setSize(120,40);
        add(namePwdWA);

        JLabel signUpLink = new JLabel("<html><u>还没有账号？点击注册</u></html>");
        signUpLink.setLocation(145, 540);
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

    private JButton createTransparentButton(String text, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // 完全透明背景，只响应点击，不显示任何内容
                // 因为图片上已经有文字，所以按钮不需要显示文字
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
    public JLabel getNamePwdWA(){
        return this.namePwdWA;
    }
}
