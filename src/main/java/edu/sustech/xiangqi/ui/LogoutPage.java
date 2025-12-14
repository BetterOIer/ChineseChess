package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

public class LogoutPage extends JFrame {

    JRoundButton submitLogout;
    JRoundButton cancelLogout;

    public LogoutPage(){
        setTitle("登出");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(200,190);
        setLocationRelativeTo(null);
        setResizable(false);

        
        JLabel delConfirm = new JLabel("你确定要登出吗?");
        delConfirm.setLocation(10, 30);
        delConfirm.setSize(200,40);
        delConfirm.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(delConfirm);


        cancelLogout = new JRoundButton("取消");
        cancelLogout.setLocation(10, 90);
        cancelLogout.setSize(60,30);
        cancelLogout.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelLogout);
        

        submitLogout = new JRoundButton("确定");
        submitLogout.setLocation(80, 90);
        submitLogout.setSize(60, 30);
        submitLogout.setFont(new Font("隶书", Font.PLAIN, 20));
        add(submitLogout);
    }
    public JButton getSubmitLogout(){
        return submitLogout;
    }
    public JButton getCancelLogout(){
        return cancelLogout;
    }
}
