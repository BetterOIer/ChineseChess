package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import edu.sustech.xiangqi.model.*;

public class ChangePwd extends JFrame {
    private JRoundPasswordField oldPwdField;
    private JRoundPasswordField newPwdField;
    private JRoundButton submitButton;
    private JRoundButton cancelButton;
    private JLabel errorLabel;

    public ChangePwd() {
        setTitle("修改密码");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JLabel oldPwdLabel = new JLabel("旧密码:");
        oldPwdLabel.setFont(new Font("隶书", Font.PLAIN, 20));
        oldPwdLabel.setLocation(10, 60);
        oldPwdLabel.setSize(120,40);
        add(oldPwdLabel);

        oldPwdField = new JRoundPasswordField();
        oldPwdField.setSize(300, 40);
        oldPwdField.setLocation(90, 60);
        oldPwdField.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 20));
        add(oldPwdField);

        JLabel newPwdLabel = new JLabel("新密码:");
        newPwdLabel.setFont(new Font("隶书", Font.PLAIN, 20));
        newPwdLabel.setLocation(10, 120);
        newPwdLabel.setSize(120,40);
        add(newPwdLabel);

        newPwdField = new JRoundPasswordField();
        newPwdField.setSize(300, 40);
        newPwdField.setLocation(90, 120);
        newPwdField.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 20));
        add(newPwdField);

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(50, 170, 300, 30);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(errorLabel);

        submitButton = new JRoundButton("确认");
        submitButton.setBounds(80, 210, 100, 30);
        submitButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(submitButton);

        cancelButton = new JRoundButton("取消");
        cancelButton.setBounds(220, 210, 100, 30);
        cancelButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelButton);

        initListeners();
    }

    private void initListeners() {
        cancelButton.addActionListener(e -> dispose());

        submitButton.addActionListener(e -> handleSubmit());

        oldPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    newPwdField.requestFocusInWindow();
                }
            }
        });

        newPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSubmit();
                }
            }
        });
    }

    private void handleSubmit() {
        try {
            User currentUser = DBOperationUser.getUserInUse();
            if (currentUser == null) {
                errorLabel.setText("未登录用户无法修改密码");
                return;
            }

            String oldPwd = new String(oldPwdField.getPassword());
            String newPwd = new String(newPwdField.getPassword());

            if (oldPwd.isEmpty() || newPwd.isEmpty()) {
                errorLabel.setText("密码不能为空");
                return;
            }

            String oldPwdHash = DBOperationUser.calHash(oldPwd);
            if (currentUser.getPswordHash() != null && !currentUser.getPswordHash().equals(oldPwdHash)) {
                errorLabel.setText("旧密码错误");
                oldPwdField.requestFocusInWindow();
                return;
            }

            String newPwdHash = DBOperationUser.calHash(newPwd);
            if (DBOperationUser.updateUserPasswordHash(currentUser.getId(), newPwdHash)) {
                errorLabel.setText("");
                oldPwdField.setText("");
                newPwdField.setText("");
                dispose();
            } else {
                errorLabel.setText("密码修改失败");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            errorLabel.setText("数据库错误");
        }
    }
}
