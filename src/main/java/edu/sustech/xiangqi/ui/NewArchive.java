package edu.sustech.xiangqi.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class NewArchive extends JFrame {

    JButton submitMod;
    JButton cancelMod;
    JTextField boardName;
    JTextField description;
    public NewArchive(){
        setTitle("新建存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(683,384);
        setLocationRelativeTo(null);


        JLabel BoardNameTip = new JLabel("存档名：");
        BoardNameTip.setLocation(10, 60);
        BoardNameTip.setSize(120,40);
        add(BoardNameTip);
        
        boardName = new JTextField();
        boardName.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        boardName.setLocation(60, 60);
        boardName.setSize(100, 40);
        add(boardName);

        JLabel descriptionTip = new JLabel("描述：");
        descriptionTip.setLocation(10, 120);
        descriptionTip.setSize(120,40);
        add(descriptionTip);
        
        description = new JTextField();
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
