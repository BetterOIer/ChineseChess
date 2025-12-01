package edu.sustech.xiangqi.ui;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.sustech.xiangqi.model.DBOperationBoard;

import javax.swing.JButton;

public class DelArchive extends JFrame {

    JButton submitMod;
    JButton cancelMod;

    public DelArchive(int idx){
        setTitle("新建存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);

        try{
            JLabel delConfirm = new JLabel("你确定要删除棋盘\""+DBOperationBoard.getBoardById(idx).getName()+"\"吗?");
            delConfirm.setLocation(10, 60);
            delConfirm.setSize(500,40);
            add(delConfirm);
        }catch(SQLException e){
            e.printStackTrace();
        }

        cancelMod = new JButton("取消");
        cancelMod.setLocation(10, 160);
        cancelMod.setSize(60,30);
        add(cancelMod);
        

        submitMod = new JButton("确定");
        submitMod.setLocation(80, 160);
        submitMod.setSize(60, 30);
        add(submitMod);
    }
    public JButton getSubmitMod(){
        return submitMod;
    }
    public JButton getCancelMod(){
        return cancelMod;
    }
}
