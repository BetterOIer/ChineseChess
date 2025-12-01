package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.DBOperationBoard;
import edu.sustech.xiangqi.model.DBOperationUser;

public class WelcomePage extends JFrame{

    private Image backgroundImage;
    private JButton archiveButton, pvpButton, aiButton;

    private void switchToLoginPage(){
        LoginPage loginPage = new LoginPage();
        setVisible(false);
        loginPage.setVisible(true);
    }

    private void switchToArchMgr() throws SQLException{
        List<ChessBoardModel> archives = DBOperationBoard.getAllBoards();
        ArchiveManager archiveManager = new ArchiveManager(archives);
        setVisible(false);
        archiveManager.setVisible(true);
    }

    private void switchToPvPPage() {
        // 双人对弈跳转到联机游戏页面
        switchToLoginPage();
    }

    private void switchToAIPage() {
        // 人机对战页面跳转到人机对战页面
        JOptionPane.showMessageDialog(this, "人机对战功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    // 获取屏幕尺寸，设置一个不铺满屏幕的正方形窗口
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    // 设置窗口大小为屏幕较小边长的70%，确保不铺满屏幕
    int squareSize = (int) (Math.min(screenWidth, screenHeight) * 0.7);

    public WelcomePage(){
        setTitle("中国象棋");
        setLayout(null);

        setSize(squareSize, squareSize);

        // 加载背景图片
        try {
            ImageIcon icon = new ImageIcon("src/main/image/WelcomePageBackground.png");
            backgroundImage = icon.getImage();
        }
        catch
        (Exception e) {
            JOptionPane.showMessageDialog(this, "背景图片加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            // 如果图片加载失败，使用默认大小
            setSize(768, 768);
            getContentPane().setBackground(new Color(220, 179, 92));
        }

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        try{
            DBOperationBoard.createTable();
            DBOperationUser.createTable();
        }catch(SQLException e){
            e.printStackTrace();
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

        // 根据图片上文字的位置创建透明按钮
        createTransparentButtons();
    }

    private void createTransparentButtons() {
        int windowWidth = getWidth();
        int windowHeight = getHeight();

        // 存档按钮 - 对应图片上的"存档"文字位置
        archiveButton = createTransparentButton("", 150, 40);
        int archiveButtonX = squareSize / 2 - 75; // 水平居中
        int archiveButtonY = squareSize * 2 / 3 - 60;
        archiveButton.setBounds(archiveButtonX, archiveButtonY, 150, 40);
        archiveButton.addActionListener(e1->{
            try {
                switchToArchMgr();
            }
            catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "数据库错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 双人对弈按钮 - 对应图片上的"双人对弈"文字位置
        pvpButton = createTransparentButton("", 150, 40);
        int pvpButtonX = squareSize / 2 - 75;
        int pvpButtonY = archiveButtonY + 70; // 在存档按钮下方60像素（对应"双人对弈"文字位置）
        pvpButton.setBounds(pvpButtonX, pvpButtonY, 150, 40);
        pvpButton.addActionListener(e1->{
            try {
                switchToPvPPage();
            }
            catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "页面跳转错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 人机对战按钮 - 对应图片上的"人机对战"文字位置
        aiButton = createTransparentButton("", 150, 40);
        int aiButtonX = windowWidth / 2 - 75;
        int aiButtonY = pvpButtonY + 60; // 在双人对弈按钮下方60像素（对应"人机对战"文字位置）
        aiButton.setBounds(aiButtonX, aiButtonY, 150, 40);
        aiButton.addActionListener(e1->{
            try {
                switchToAIPage();
            }
            catch
            (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, "页面跳转错误: " + e.getMessage(), "错误"
                        , JOptionPane.ERROR_MESSAGE);
            }
        });

        // 添加按钮到面板
        getContentPane().add(archiveButton);
        getContentPane().add(pvpButton);
        getContentPane().add(aiButton);
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
                        new Color(255, 255, 255, 100), 2
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomePage welcomePage = new WelcomePage();
            welcomePage.setVisible(true);});
    }
}
