package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpPage extends JFrame{

    private JRoundTextField userName;
    private JRoundPasswordField password;
    private JButton signUp;
    private JLabel userNameOccupied;
    private JLabel userNameInvalid;
    private JLabel passwordInvalid;

    private Image backgroundImage;

    // 获取屏幕尺寸，设置一个不铺满屏幕的正方形窗口
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    // 设置窗口大小为屏幕较小边长的70%，确保不铺满屏幕
    int squareSize = (int) (Math.min(screenWidth, screenHeight) * 0.7);

    public SignUpPage(){
        setTitle("中国象棋-注册");
        setLayout(null);

        setSize(squareSize, squareSize);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        // 设置组件位置和大小（使用相对定位）
        setupComponents(backgroundPanel);

        setResizable(false);

    }

    private void setupComponents(JPanel backgroundPanel) {
        CreateTransparentButton transparentButton = new CreateTransparentButton();
        CreateText text = new CreateText();

        JLabel userNameTip = text.createText("用户名：", 100, 40);
        int userNameTipX = squareSize / 4;
        int userNameTipY = squareSize * 2 / 3 - 60;
        userNameTip.setBounds(userNameTipX, userNameTipY, 100, 40);
        userNameTip.setSize(100,40);
        add(userNameTip);

        userName = new JRoundTextField();
        userName.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 20));
        userName.setLocation(userNameTipX + 120, userNameTipY);
        userName.setSize(squareSize/4*2-120, 40);
        add(userName);

        userNameOccupied = new JLabel("用户已存在！");
        userNameOccupied.setForeground(java.awt.Color.RED);
        userNameOccupied.setVisible(false);
        userNameOccupied.setLocation(userNameTipX + 120, userNameTipY+30);
        userNameOccupied.setSize(120,40);
        add(userNameOccupied);

        userNameInvalid = new JLabel("用户名为空！");
        userNameInvalid.setForeground(java.awt.Color.RED);
        userNameInvalid.setVisible(false);
        userNameInvalid.setLocation(userNameTipX + 120, userNameTipY+30);
        userNameInvalid.setSize(120,40);
        add(userNameInvalid);

        passwordInvalid = new JLabel("密码为空！");
        passwordInvalid.setForeground(java.awt.Color.RED);
        passwordInvalid.setVisible(false);
        passwordInvalid.setLocation(userNameTipX + 120, userNameTipY + 100);
        passwordInvalid.setSize(120,40);
        add(passwordInvalid);

        JLabel passwordTip = text.createText("密码：", 100, 40);
        passwordTip.setBounds(userNameTipX, userNameTipY + 70, 100,40);
        passwordTip.setSize(100,40);
        add(passwordTip);

        password = new JRoundPasswordField();
        password.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 20));
        password.setLocation(userNameTipX + 120, userNameTipY + 70);
        password.setSize(squareSize/4*2-120, 40);
        add(password);

        signUp = transparentButton.createTransparentButton("注册", 150, 40);
        signUp.setLocation((squareSize-150)/2, userNameTipY + 140);
        signUp.setSize(150, 40);
        add(signUp);
    }
    public JTextField getUserName(){
        return this.userName;
    }
    public JRoundPasswordField getPassWord(){
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
