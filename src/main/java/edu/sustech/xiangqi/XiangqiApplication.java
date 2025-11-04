package edu.sustech.xiangqi;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.ui.ChessBoardPanel;

import javax.swing.*;

public class XiangqiApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("login");
            loginFrame.setLayout(null);
            loginFrame.setSize(300, 300);
            JTextField username = new JTextField();
            username.setLocation(100, 10);
            username.setSize(100, 50);
            loginFrame.add(username);
            JTextField password = new JTextField();
            password.setLocation(100,80);
            password.setSize(100,50);
            loginFrame.add(password);
            JLabel username1 = new JLabel("username");
            username1.setSize(100,30);
            username1.setLocation(30, 10);
            loginFrame.add(username1);
            JLabel password1 = new JLabel("password");
            password1.setSize(100,30);
            password1.setLocation(30, 80);
            loginFrame.add(password1);

            JButton button1 = new JButton("login");
            button1.setSize(100, 50);
            button1.setLocation(100, 200);
            loginFrame.add(button1);
            loginFrame.setVisible(true);
            button1.addActionListener(e1->{
                String u = username.getText();
                String p = password.getText();
                if(!u.equals("") && !p.equals ("")){
                    loginFrame.setVisible(false);
                    JFrame frame = new JFrame("中国象棋");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JLabel label = new JLabel("开始");
                    label.setSize(100,50);
                    label.setLocation(600, 100);
                    frame.add(label);

                    JButton button = new JButton("start");
                    button.setLocation(600, 300);
                    button.setSize(100, 50);
                    button.addActionListener(e->{
                        String s = username.getText();
                        label.setText(s);
                        loginFrame.setVisible(false);
                        frame.setVisible(true);
                    });
                    frame.add(button);

                    ChessBoardModel model = new ChessBoardModel();
                    ChessBoardPanel boardPanel = new ChessBoardPanel(model);

                    frame.add(boardPanel);
                    frame.setSize(800, 700);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });


        });
    }
}
