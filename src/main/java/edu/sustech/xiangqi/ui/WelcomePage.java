package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import edu.sustech.xiangqi.model.*;

public class WelcomePage extends JFrame{

    private LogoutPage logoutPage;
    private ChangePwd changePwdPage;
    private Image backgroundImage;
    private JButton loginButton, archiveButton, pvpButton, aiButton;
    private JLabel logoutButton, userInUse, changePwd;

    private boolean back2Login=false;

    CreateTransparentButton transparentButton = new CreateTransparentButton();

    // 获取屏幕尺寸7
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;

    // 设置窗口大小为屏幕较小边长的70%7
    int squareSize = (int) (Math.min(screenWidth, screenHeight) * 0.7);

    public WelcomePage() {
        this(true);
    }

    public WelcomePage(boolean shouldLogout){
        Style.initGlobalFont();
        setTitle("中国象棋");
        setLayout(null);

        setSize(squareSize, squareSize);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 加载背景图片
        try {
            ImageIcon icon = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/WelcomePageBackground2.png");
            backgroundImage = icon.getImage();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "背景图片加载失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            // 如果图片加载失败，使用默认大小
            setSize(768, 768);
            getContentPane().setBackground(new Color(220, 179, 92));
        }
        setResizable(false);
        try{
            DBOperationBoard.createTable();
            DBOperationUser.createTable();
            DBOperationBoard.deleteBoardsOfNull();
            if (shouldLogout) {
                DBOperationUser.logoutAll();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        logoutPage = new LogoutPage();
        changePwdPage = new ChangePwd();
        logoutPage.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
        logoutPage.getRootPane().getActionMap().put("confirm", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
        logoutPage.getSubmitLogout().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLogout();
            }
        });
        logoutPage.getCancelLogout().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logoutPage.dispose();
            }
        });

        loginButton = transparentButton.createTransparentButton("登录", 90, 40);
        loginButton.setLocation(squareSize - 110, 20);
        loginButton.setSize(90, 40);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToLoginPage(true);
            }
        });

        userInUse = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                String text = getText();
                if (text == null || text.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(UIManager.getFont("Button.font").deriveFont(Font.BOLD, 20f));
                int textWidth = g2.getFontMetrics().stringWidth(text);
                int h = getHeight();
                int w = getWidth();

                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(w - textWidth - 15, (h - 30) / 2, textWidth + 10, 30, 10, 10);
                g2.setColor(Color.GRAY);
                g2.drawString(text, w - textWidth - 10, (h - 30) / 2 + 21);
                g2.dispose();
            }
        };
        userInUse.setSize(300, 35);
        userInUse.setLocation(squareSize - 320, 20);
        userInUse.setVisible(false);

        changePwd = new JLabel("修改密码");
        changePwd.setHorizontalAlignment(SwingConstants.RIGHT);
        changePwd.setLocation(squareSize-125, 60);
        changePwd.setSize(100, 30);
        changePwd.setFont(new Font("隶书", Font.PLAIN, 20));
        changePwd.setVisible(false);
        changePwd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToChangePwdPage();
            }
        });

        logoutButton = new JLabel("登出");
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setLocation(squareSize-125, 90);
        logoutButton.setSize(100, 40);
        logoutButton.setFont(new Font("隶书", Font.PLAIN, 20));
        logoutButton.setVisible(false);
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToLogoutPage();
            }
        });

        userInUse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userInUse.isVisible()) {
                    logoutButton.setVisible(!logoutButton.isVisible());
                    changePwd.setVisible(!changePwd.isVisible());
                }
            }
        });

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
        getContentPane().add(loginButton);
        getContentPane().add(logoutButton);
        getContentPane().add(changePwd);
        getContentPane().add(userInUse);
        // 创建透明按钮
        createTransparentButtons();

        if (!shouldLogout) {
            try {
                User currentUser = DBOperationUser.getUserInUse();
                if (!currentUser.getName().equals("null")) {
                    userInUse.setText("当前用户：" + currentUser.getName());
                    userInUse.setVisible(true);
                    loginButton.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogout(){
        try{
            User tmp = DBOperationUser.getUserInUse();
            tmp.setType((tmp.getType()^4));
            DBOperationUser.updateUserType(tmp.getId(),tmp.getType());
        }catch(SQLException e2){
            e2.printStackTrace();
        }
        logoutButton.setVisible(false);
        changePwd.setVisible(false);
        userInUse.setVisible(false);
        loginButton.setVisible(true);
        logoutPage.setVisible(false);
        switchToTourWarning();
    }

    public void switchToLoginPage(boolean force){
        setVisible(false);
        LoginPage loginPage = new LoginPage(force);
        loginPage.setVisible(true);
        loginPage.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                handleLogin(loginPage);
            }
        });
        KeyAdapter enterKeyAdapterForUserName = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginPage.getPassword().requestFocusInWindow();
                }
            }
        };
        KeyAdapter enterKeyAdapterForPwd = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin(loginPage);
                }
            }
        };
        loginPage.getUserName().addKeyListener(enterKeyAdapterForUserName);
        loginPage.getPassword().addKeyListener(enterKeyAdapterForPwd);
        loginPage.getTourLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                loginPage.dispose();
            }
        });
        loginPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                if(!userInUse.isVisible())switchToTourWarning();
            }
        });
    }
    private void handleLogin(LoginPage loginPage){
        try{
            User userTmp = DBOperationUser.getUserByName(loginPage.getUserName().getText());
            String inputPwd = new String(loginPage.getPassword().getPassword());
            if(userTmp==null || (!DBOperationUser.calHash(inputPwd).equals(userTmp.getPswordHash()))){
                loginPage.getNamePwdWA().setVisible(true);
            }else if(DBOperationUser.calHash(inputPwd).equals(userTmp.getPswordHash())){
                loginPage.getNamePwdWA().setVisible(false);
                DBOperationUser.logoutAll();
                userTmp.setType((userTmp.getType()|4));
                DBOperationUser.updateUserType(userTmp.getId(), userTmp.getType());
                userInUse.setText("当前用户："+userTmp.getName());

                userInUse.setVisible(true);
                loginPage.dispose();
                loginButton.setVisible(false);
                setVisible(true);
            }
        }catch(SQLException e2){
            e2.printStackTrace();
        }
    }

    private void switchToChangePwdPage() {
        changePwdPage.setVisible(true);
    }

    private void switchToLogoutPage(){
        logoutPage.setVisible(true);
    }

    private void switchToTourWarning(){
        TourWarning tourWarning = new TourWarning();
        back2Login=false;
        tourWarning.setVisible(true);
        tourWarning.getCancelAcknow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                back2Login=true;
                tourWarning.dispose();
                switchToLoginPage(true);
            }
        });
        tourWarning.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirm");
        tourWarning.getRootPane().getActionMap().put("confirm", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                tourWarning.dispose();
            }
        });
        tourWarning.getSubmitAcknow().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(true);
                tourWarning.dispose();
            }
        });
        tourWarning.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e){
                if(!back2Login)setVisible(true);
                tourWarning.dispose();
            }
        });
    }

    private void switchToArchMgr() throws SQLException{
        List<ChessBoardModel> archives = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse());
        ArchiveManager archiveManager = new ArchiveManager(archives);
        dispose();
        archiveManager.setVisible(true);
    }

    private void switchToConnection(){
        try{
            if(DBOperationUser.getUserInUse()==null || DBOperationUser.getUserInUse().getName().equals("null")){
                switchToTourWarning();
            }else{
                Connection connection = new Connection();
                connection.setVisible(true);
                dispose();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void switchToAIPage() {
        JOptionPane.showMessageDialog(this, "人机对战功能开发中...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createTransparentButtons() {
        int windowWidth = getWidth();
        int windowHeight = getHeight();

        archiveButton = transparentButton.createTransparentButton("本地棋局", 150, 45);
        int archiveButtonX = (squareSize-150)/2; // 水平居中
        int archiveButtonY = squareSize * 2 / 3 - 60;
        archiveButton.setBounds(archiveButtonX, archiveButtonY, 150, 45);
        archiveButton.addActionListener(e1->{
            try {
                if(DBOperationUser.getUserInUse()==null){
                    User userTmp = DBOperationUser.getUserByName("null");
                    DBOperationUser.logoutAll();
                    userTmp.setType(userTmp.getType()|4);
                    DBOperationUser.updateUserType(userTmp.getId(), userTmp.getType());
                }
                switchToArchMgr();
            }
            catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "数据库错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        pvpButton = transparentButton.createTransparentButton("双人对弈", 150, 45);
        int pvpButtonX = (squareSize-150)/2;
        int pvpButtonY = archiveButtonY + 70; // 在存档按钮下方60像素（对应"双人对弈"文字位置）
        pvpButton.setBounds(pvpButtonX, pvpButtonY, 150, 45);
        pvpButton.addActionListener(e1->{
            try {
                switchToConnection();
            }
            catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "页面跳转错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        aiButton = transparentButton.createTransparentButton("人机对战", 150, 45);
        int aiButtonX = (squareSize-150)/2;
        int aiButtonY = pvpButtonY + 70; // 在双人对弈按钮下方60像素（对应"人机对战"文字位置）
        aiButton.setBounds(aiButtonX, aiButtonY, 150, 45);
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
}