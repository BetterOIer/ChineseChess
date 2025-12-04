package edu.sustech.xiangqi.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JButton;

public class TourWarning extends JFrame {

    JButton submitAcknow;
    JButton cancelAcknow;

    public TourWarning(){
        setTitle("提示");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);

        
        JLabel delConfirm = new JLabel("使用游客模式登录，你将只能开启单人模式");
        delConfirm.setLocation(10, 60);
        delConfirm.setSize(500,40);
        add(delConfirm);


        cancelAcknow = new JButton("去登录");
        cancelAcknow.setLocation(10, 160);
        cancelAcknow.setSize(60,30);
        add(cancelAcknow);
        

        submitAcknow = new JButton("了解");
        submitAcknow.setLocation(80, 160);
        submitAcknow.setSize(60, 30);
        add(submitAcknow);
    }
    public JButton getSubmitAcknow(){
        return submitAcknow;
    }
    public JButton getCancelAcknow(){
        return cancelAcknow;
    }
}
