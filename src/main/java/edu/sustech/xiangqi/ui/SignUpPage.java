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

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 加载背景图片
        try {
            ImageIcon icon = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/SignUpPage.png");
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
        userNameTip.setLocation(10, 60);
        userNameTip.setSize(120,40);
        add(userNameTip);

         */

        userName = new JRoundTextField();
        userName.setLocation(280, 360);
        userName.setSize(160, 50);
        add(userName);

        userNameOccupied = new JLabel("用户已存在！");
        userNameOccupied.setForeground(java.awt.Color.RED);
        userNameOccupied.setVisible(false);
        userNameOccupied.setLocation(480, 360);
        userNameOccupied.setSize(120,40);
        add(userNameOccupied);

        userNameInvalid = new JLabel("用户名为空！");
        userNameInvalid.setForeground(java.awt.Color.RED);
        userNameInvalid.setVisible(false);
        userNameInvalid.setLocation(480, 360);
        userNameInvalid.setSize(120,40);
        add(userNameInvalid);

        passwordInvalid = new JLabel("密码为空！");
        passwordInvalid.setForeground(java.awt.Color.RED);
        passwordInvalid.setVisible(false);
        passwordInvalid.setLocation(180, 110);
        passwordInvalid.setSize(120,40);
        add(passwordInvalid);

        /*JLabel passwordTip = new JLabel("密码：");
        passwordTip.setLocation(280, 425);
        passwordTip.setSize(160,50);
        add(passwordTip);

         */

        password = new JRoundPasswordField();
        password.setLocation(280, 425);
        password.setSize(160, 50);
        add(password);

        signUp = new JButton("注册");
        signUp = createTransparentButton("", 150, 40);
        signUp.setLocation(280, 495);
        signUp.setSize(100, 40);
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
