package edu.sustech.xiangqi.ui;

import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;

import edu.sustech.xiangqi.model.DBOperationBoard;

public class DelArchive extends JFrame {

    JRoundButton submitMod;
    JRoundButton cancelMod;

    public DelArchive(int idx){
        setTitle("删除存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,190);
        setLocationRelativeTo(null);
        setResizable(false);

        try{
            JLabel delConfirm = new JLabel("你确定要删除棋盘\""+DBOperationBoard.getBoardById(idx).getName()+"\"吗?");
            delConfirm.setLocation(20, 40);
            delConfirm.setSize(460,40);
            delConfirm.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
            add(delConfirm);
        }catch(SQLException e){
            e.printStackTrace();
        }

        cancelMod = new JRoundButton("取消");
        cancelMod.setLocation(20, 100);
        cancelMod.setSize(60,30);
        cancelMod.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelMod);
        

        submitMod = new JRoundButton("确定");
        submitMod.setLocation(90, 100);
        submitMod.setSize(60, 30);
        submitMod.setFont(new Font("隶书", Font.PLAIN, 20));
        add(submitMod);
    }
    public JButton getSubmitMod(){
        return submitMod;
    }
    public JButton getCancelMod(){
        return cancelMod;
    }
}
