package edu.sustech.xiangqi.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;

public class NewArchive extends JFrame {

    JRoundButton submitMod;
    JRoundButton cancelMod;
    JRoundTextField boardName;
    JRoundTextField description;
    
    private JRoundRadioButton redFirst;
    private JRoundRadioButton randomFirst;
    private JRoundRadioButton blackFirst;

    public NewArchive(){
        setTitle("新建存档");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430,360);
        setBackground(Style.defaultColor);
        setLocationRelativeTo(null);
        setResizable(false);


        JLabel BoardNameTip = new JLabel("名称：");
        BoardNameTip.setLocation(10, 60);
        BoardNameTip.setSize(120,40);
        BoardNameTip.setFont(new Font("隶书", Font.PLAIN, 20));
        add(BoardNameTip);
        
        boardName = new JRoundTextField();
        boardName.setText("未命名-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
        description.setLocation(90, 120);
        description.setSize(300, 40);
        description.setFont(UIManager.getFont("Button.font").deriveFont(Font.PLAIN, 17f));
        add(description);

        JLabel firstTip = new JLabel("先手：");
        firstTip.setLocation(10, 180);
        firstTip.setSize(120,40);
        firstTip.setFont(new Font("隶书", Font.PLAIN, 20));
        add(firstTip);

        redFirst = new JRoundRadioButton("红先");
        redFirst.setLocation(90, 185);
        redFirst.setSize(60, 30);
        redFirst.setFont(UIManager.getFont("Button.font").deriveFont(Font.BOLD, 19f));
        redFirst.setSelected(true);
        add(redFirst);

        randomFirst = new JRoundRadioButton("随机");
        randomFirst.setLocation(160, 185);
        randomFirst.setSize(60, 30);
        randomFirst.setFont(UIManager.getFont("Button.font").deriveFont(Font.BOLD, 19f));
        add(randomFirst);

        blackFirst = new JRoundRadioButton("黑先");
        blackFirst.setLocation(230, 185);
        blackFirst.setSize(60, 30);
        blackFirst.setFont(UIManager.getFont("Button.font").deriveFont(Font.BOLD, 19f));
        add(blackFirst);

        ButtonGroup group = new ButtonGroup();
        group.add(redFirst);
        group.add(randomFirst);
        group.add(blackFirst);

        cancelMod = new JRoundButton("取消");
        cancelMod.setLocation(10, 240);
        cancelMod.setSize(60,30);
        cancelMod.setFont(new Font("隶书", Font.PLAIN, 20));
        add(cancelMod);
        

        submitMod = new JRoundButton("保存");
        submitMod.setLocation(80, 240);
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

    public boolean getWhoseTurn(){
        if(redFirst.isSelected()) return true;
        if(blackFirst.isSelected()) return false;
        return Math.random()>0.5;
    }
}
