package edu.sustech.xiangqi.ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import edu.sustech.xiangqi.model.DBOperationUser;
import edu.sustech.xiangqi.model.User;

public class LoginPage extends JFrame{

    JButton loginButton,tourLoginButton;
    JTextField userName,password;
    boolean force;
    private Image backgroundImage;

    // 获取屏幕尺寸，设置一个不铺满屏幕的正方形窗口
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    // 设置窗口大小为屏幕较小边长的70%，确保不铺满屏幕
    int squareSize = (int) (Math.min(screenWidth, screenHeight) * 0.7);

    public LoginPage(boolean force){
        setTitle("中国象棋-登录");
        setLayout(null);

        setSize(squareSize, squareSize);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 加载背景图片
        try {
            ImageIcon icon = new ImageIcon("src/main/image/LoginPage.png");
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

        // 设置组件位置和大小（使用相对定位）
        setupComponents(backgroundPanel);

        setResizable(false);

        }

    private void setupComponents(JPanel backgroundPanel) {
        /*JLabel userNameTip = new JLabel("用户名：");
        userNameTip.setLocation(145, 360);
        userNameTip.setSize(100,40);
        add(userNameTip);

         */

        userName = new JTextField();
        userName.setLocation(280, 360);
        userName.setSize(160, 50);
        add(userName);

        /*JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(150, 430);
        passwordTip.setSize(120,40);
        add(passwordTip);

         */

        password = new JTextField();
        password.setLocation(280, 425);
        password.setSize(160, 50);
        add(password);

        loginButton = new JButton("登录");
        loginButton = createTransparentButton("", 150, 40);
        loginButton.setLocation(160, 495);
        loginButton.setSize(100, 40);
        add(loginButton);

        tourLoginButton = new JButton("仅游客登录");
        tourLoginButton = createTransparentButton("", 150, 40);
        tourLoginButton.setLocation(400, 495);
        tourLoginButton.setSize(100, 40);
        tourLoginButton.setVisible(!force);
        add(tourLoginButton);

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
                    }else if(signUpPage.getPassWord().getText().equals("")){
                        signUpPage.getPasswordInvalid().setVisible(true);
                    }else{
                        DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(), signUpPage.getUserName().getText(), signUpPage.getPassWord().getText(), 1));
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
    public JTextField getPassword(){
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
}
