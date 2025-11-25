package edu.sustech.xiangqi.ui;

import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.sustech.xiangqi.model.DBOperationBoard;

public class ModifyArchive extends JFrame {

    JButton submitMod;
    JButton cancelMod;
    JTextField boardName;
    JTextField description;
    public ModifyArchive(int idx){
        setTitle("修改存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);


        JLabel BoardNameTip = new JLabel("存档名：");
        BoardNameTip.setLocation(10, 60);
        BoardNameTip.setSize(120,40);
        add(BoardNameTip);
        
        boardName = new JTextField();
        try{
            String n = DBOperationBoard.getBoardById(idx).getName();
            if(n != null) boardName.setText(n);
        }catch(SQLException e){
            e.printStackTrace();
        }
        boardName.setLocation(60, 60);
        boardName.setSize(100, 40);
        add(boardName);

        JLabel descriptionTip = new JLabel("描述：");
        descriptionTip.setLocation(10, 120);
        descriptionTip.setSize(120,40);
        add(descriptionTip);
        
        description = new JTextField();
        try{
            String d = DBOperationBoard.getBoardById(idx).getDescription();
            if(d != null) description.setText(d);
        }catch(SQLException e){
            e.printStackTrace();
        }
        description.setLocation(60, 120);
        description.setSize(100, 40);
        add(description);

        cancelMod = new JButton("取消");
        cancelMod.setLocation(10, 160);
        cancelMod.setSize(60,30);
        add(cancelMod);
        

        submitMod = new JButton("保存");
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
    public JTextField getBoardName(){
        return boardName;
    }
    public JTextField getDescription(){
        return description;
    }
}
