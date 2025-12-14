package edu.sustech.xiangqi.ui;

import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;

import edu.sustech.xiangqi.model.DBOperationBoard;
import edu.sustech.xiangqi.model.DBOperationUser;

public class ModifyArchive extends JFrame {

    JRoundButton submitMod;
    JRoundButton cancelMod;
    JRoundTextField boardName;
    JRoundTextField description;
    public ModifyArchive(int idx){
        setTitle("修改存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430,300);
        setLocationRelativeTo(null);
        setResizable(false);


        JLabel BoardNameTip = new JLabel("名称：");
        BoardNameTip.setLocation(10, 60);
        BoardNameTip.setSize(120,40);
        BoardNameTip.setFont(new Font("隶书", Font.PLAIN, 20));
        add(BoardNameTip);
        
        boardName = new JRoundTextField();
        try{
            String n = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse()).get(idx).getName();
            if(n != null) boardName.setText(n);
        }catch(SQLException e){
            e.printStackTrace();
        }
        boardName.setLocation(90, 60);
        boardName.setSize(300, 40);
        boardName.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(boardName);

        JLabel descriptionTip = new JLabel("描述：");
        descriptionTip.setLocation(10, 120);
        descriptionTip.setSize(120,40);
        descriptionTip.setFont(new Font("隶书", Font.PLAIN, 20));
        add(descriptionTip);
        
        description = new JRoundTextField();
        try{
            String d = DBOperationBoard.getBoardsByUser(DBOperationUser.getUserInUse()).get(idx).getDescription();
            if(d != null) description.setText(d);
        }catch(SQLException e){
            e.printStackTrace();
        }
        description.setLocation(90, 120);
        description.setSize(300, 40);
        description.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(description);

        cancelMod = new JRoundButton("取消");
        cancelMod.setLocation(10, 180);
        cancelMod.setSize(60,30);
        cancelMod.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelMod);
        

        submitMod = new JRoundButton("保存");
        submitMod.setLocation(80, 180);
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
    public JTextField getBoardName(){
        return boardName;
    }
    public JTextField getDescription(){
        return description;
    }
}
