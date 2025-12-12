package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

import java.awt.Font;

public class TourWarning extends JFrame {

    JRoundButton submitAcknow;
    JRoundButton cancelAcknow;

    public TourWarning(){
        setTitle("提示");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,190);
        setLocationRelativeTo(null);
        setResizable(false);

        
        JLabel delConfirm = new JLabel("使用游客模式登录，你只能开启单人模式！");
        delConfirm.setLocation(10, 40);
        delConfirm.setSize(400,30);
        delConfirm.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(delConfirm);


        cancelAcknow = new JRoundButton("去登录");
        cancelAcknow.setLocation(10, 90);
        cancelAcknow.setSize(80,30);
        cancelAcknow.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelAcknow);
        

        submitAcknow = new JRoundButton("了解");
        submitAcknow.setLocation(110, 90);
        submitAcknow.setSize(80, 30);
        submitAcknow.setFont(new Font("隶书", Font.PLAIN, 20));
        add(submitAcknow);
    }
    public JButton getSubmitAcknow(){
        return submitAcknow;
    }
    public JButton getCancelAcknow(){
        return cancelAcknow;
    }
}
