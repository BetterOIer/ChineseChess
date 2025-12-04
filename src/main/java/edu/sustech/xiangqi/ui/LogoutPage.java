package edu.sustech.xiangqi.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JButton;

public class LogoutPage extends JFrame {

    JButton submitLogout;
    JButton cancelLogout;

    public LogoutPage(){
        setTitle("登出");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);

        
        JLabel delConfirm = new JLabel("你确定要登出吗?");
        delConfirm.setLocation(10, 60);
        delConfirm.setSize(500,40);
        add(delConfirm);


        cancelLogout = new JButton("取消");
        cancelLogout.setLocation(10, 160);
        cancelLogout.setSize(60,30);
        add(cancelLogout);
        

        submitLogout = new JButton("确定");
        submitLogout.setLocation(80, 160);
        submitLogout.setSize(60, 30);
        add(submitLogout);
    }
    public JButton getSubmitLogout(){
        return submitLogout;
    }
    public JButton getCancelLogout(){
        return cancelLogout;
    }
}
