package edu.sustech.xiangqi.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JButton;

public class TourWarning extends JFrame {

    JRoundButton submitAcknow;
    JRoundButton cancelAcknow;

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


        cancelAcknow = new JRoundButton("去登录");
        cancelAcknow.setLocation(10, 160);
        cancelAcknow.setSize(90,30);
        add(cancelAcknow);
        

        submitAcknow = new JRoundButton("了解");
        submitAcknow.setLocation(110, 160);
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
