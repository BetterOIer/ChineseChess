package edu.sustech.xiangqi.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class NewArchive extends JFrame {

    JRoundButton submitMod;
    JRoundButton cancelMod;
    JRoundTextField boardName;
    JRoundTextField description;
    
    private javax.swing.JRadioButton redFirst;
    private javax.swing.JRadioButton randomFirst;
    private javax.swing.JRadioButton blackFirst;

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
        
        boardName = new JRoundTextField();
        boardName.setText("未命名-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        boardName.setLocation(60, 60);
        boardName.setSize(200, 40);
        add(boardName);

        JLabel descriptionTip = new JLabel("描述：");
        descriptionTip.setLocation(10, 120);
        descriptionTip.setSize(120,40);
        add(descriptionTip);
        
        description = new JRoundTextField();
        description.setLocation(60, 120);
        description.setSize(200, 40);
        add(description);

        JLabel firstTip = new JLabel("先手：");
        firstTip.setLocation(10, 180);
        firstTip.setSize(120,40);
        add(firstTip);

        redFirst = new javax.swing.JRadioButton("红先");
        redFirst.setLocation(60, 180);
        redFirst.setSize(60, 40);
        redFirst.setSelected(true);
        add(redFirst);

        randomFirst = new javax.swing.JRadioButton("随机");
        randomFirst.setLocation(120, 180);
        randomFirst.setSize(60, 40);
        add(randomFirst);

        blackFirst = new javax.swing.JRadioButton("黑先");
        blackFirst.setLocation(180, 180);
        blackFirst.setSize(60, 40);
        add(blackFirst);

        javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
        group.add(redFirst);
        group.add(randomFirst);
        group.add(blackFirst);

        cancelMod = new JRoundButton("取消");
        cancelMod.setLocation(10, 240);
        cancelMod.setSize(60,30);
        add(cancelMod);
        

        submitMod = new JRoundButton("保存");
        submitMod.setLocation(80, 240);
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

    public boolean getWhoseTurn(){
        if(redFirst.isSelected()) return true;
        if(blackFirst.isSelected()) return false;
        return Math.random()>0.5;
    }
}
