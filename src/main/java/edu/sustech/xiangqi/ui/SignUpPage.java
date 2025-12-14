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
        JLabel userNameTip = createTextField("用户名：", 100, 40);
        int userNameTipX = squareSize / 4;
        int userNameTipY = squareSize * 2 / 3 - 60;
        userNameTip.setBounds(userNameTipX, userNameTipY, 150, 45);
        userNameTip.setSize(150,40);
        add(userNameTip);

        userName = new JRoundTextField();
        userName.setLocation(userNameTipX + 120, userNameTipY);
        userName.setSize(160, 42);
        add(userName);

        userNameOccupied = new JLabel("用户已存在！");
        userNameOccupied.setForeground(java.awt.Color.RED);
        userNameOccupied.setVisible(false);
        userNameOccupied.setLocation(userNameTipX + 300, userNameTipY);
        userNameOccupied.setSize(120,40);
        add(userNameOccupied);

        userNameInvalid = new JLabel("用户名为空！");
        userNameInvalid.setForeground(java.awt.Color.RED);
        userNameInvalid.setVisible(false);
        userNameInvalid.setLocation(userNameTipX + 300, userNameTipY);
        userNameInvalid.setSize(120,40);
        add(userNameInvalid);

        passwordInvalid = new JLabel("密码为空！");
        passwordInvalid.setForeground(java.awt.Color.RED);
        passwordInvalid.setVisible(false);
        passwordInvalid.setLocation(userNameTipX + 300, userNameTipY + 70);
        passwordInvalid.setSize(120,40);
        add(passwordInvalid);

        JLabel passwordTip = createTextField("密码：", 100, 40);
        passwordTip.setBounds(userNameTipX, userNameTipY + 70, 150, 45);
        passwordTip.setSize(150,40);
        add(passwordTip);

        password = new JRoundPasswordField();
        password.setLocation(userNameTipX + 120, userNameTipY + 70);
        password.setSize(160, 42);
        add(password);

        signUp = createTransparentButton("注册", 150, 40);
        signUp.setLocation(userNameTipX + 100, userNameTipY + 140);
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
                // 定义棕色线段的颜色和厚度
                final Color LINE_COLOR = new Color(185, 145, 110); // 咖啡色/棕色
                final int LINE_THICKNESS = 2; // 线段厚度
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 2. 绘制顶部棕色线段（y坐标为0，厚度LINE_THICKNESS）
                g2.setColor(LINE_COLOR);
                g2.setStroke(new BasicStroke(LINE_THICKNESS)); // 设置线段厚度
                // 线段起点(x1=0, y1=0)，终点(x2=width, y2=0)，覆盖按钮宽度
                g2.drawLine(0, 0, width, 0);

                // 3. 绘制底部棕色线段（y坐标为height-1，避免超出按钮边界）
                // 线段起点(x1=0, y1=height-1)，终点(x2=width, y2=height-1)
                g2.drawLine(0, height - 1, width, height - 1);

                // 绘制按钮文字（必须保留，否则文字不显示）
                g.setFont(new Font("隶书", Font.BOLD, 22));
                g.setColor(new Color(111, 78, 55));
                super.paintComponent(g);
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
    public static JLabel createTextField(String text, int width, int height) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // 完全透明背景，只响应点击，不显示任何内容
                // 因为图片上已经有文字，所以按钮不需要显示文字
                // 定义棕色线段的颜色和厚度
                final Color LINE_COLOR = new Color(185, 145, 110); // 咖啡色/棕色
                final int LINE_THICKNESS = 2; // 线段厚度
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 2. 绘制顶部棕色线段（y坐标为0，厚度LINE_THICKNESS）
                g2.setColor(LINE_COLOR);
                g2.setStroke(new BasicStroke(LINE_THICKNESS)); // 设置线段厚度
                // 线段起点(x1=0, y1=0)，终点(x2=width, y2=0)，覆盖按钮宽度
                g2.drawLine(0, 0, width, 0);

                // 3. 绘制底部棕色线段（y坐标为height-1，避免超出按钮边界）
                // 线段起点(x1=0, y1=height-1)，终点(x2=width, y2=height-1)
                g2.drawLine(0, height - 1, width, height - 1);

                final Color textColor = Color.BLACK;
                g2.setFont(new Font("隶书", Font.PLAIN, 22));
                g2.setColor(textColor);
                // 获取字体度量信息，用于计算文字居中坐标
                FontMetrics fm = g2.getFontMetrics();
                // 计算文字水平居中：(标签宽度 - 文字宽度) / 2
                int textX = (width - fm.stringWidth(getText())) / 2;
                // 计算文字垂直居中：(标签高度 + 字体上升高度) / 2（上升高度是文字基线到顶部的距离）
                int textY = (height + fm.getAscent()) / 2 - fm.getDescent();

                // 绘制居中的文字（替代原有的super.paintComponent(g)）
                // 绘制按钮文字（必须保留，否则文字不显示）

                g2.drawString(getText(), textX, textY);

                g2.dispose(); // 释放资源
            }
        };
        return label;
    }
}
