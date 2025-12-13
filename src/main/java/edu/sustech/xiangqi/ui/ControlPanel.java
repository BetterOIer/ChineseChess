package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ControlPanel extends JPanel{

    private ChessBoardModel model;
    private ChessBoard board;

    private JRoundButton reset, playBack, backToArchive, musicOn, surrender;

    public ControlPanel(ChessBoardModel model,ChessBoard board){

        this.model=model;
        this.board=board;

        setSize(board.getWindowWidth()/10, board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBackground(Style.defaultColor);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        Dimension buttonSize = new Dimension(board.getWindowWidth()/10, (int)(board.getWindowHeight()/20));

        reset = new JRoundButton("重置棋盘");
        reset.setPreferredSize(buttonSize);
        reset.setMaximumSize(buttonSize);
        reset.setMinimumSize(buttonSize);
        reset.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/37.8)));
        reset.setFocusPainted(false);
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        if(model.getAllSteps().isEmpty())reset.setEnabled(false);
        if((model.getType()&2)!=0)reset.setEnabled(false);

        playBack = new JRoundButton("复盘");
        playBack.setPreferredSize(buttonSize);
        playBack.setMaximumSize(buttonSize);
        playBack.setMinimumSize(buttonSize);
        playBack.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/37.8)));
        playBack.setFocusPainted(false);
        playBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))==0)playBack.setEnabled(false);

        backToArchive = new JRoundButton("返回存档");
        backToArchive.setPreferredSize(buttonSize);
        backToArchive.setMaximumSize(buttonSize);
        backToArchive.setMinimumSize(buttonSize);
        backToArchive.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/37.8)));
        backToArchive.setFocusPainted(false);
        backToArchive.setAlignmentX(Component.CENTER_ALIGNMENT);

        musicOn = new JRoundButton("音乐");
        musicOn.setPreferredSize(buttonSize);
        musicOn.setMaximumSize(buttonSize);
        musicOn.setMinimumSize(buttonSize);
        musicOn.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/37.8)));
        musicOn.setFocusPainted(false);
        musicOn.setAlignmentX(Component.CENTER_ALIGNMENT);

        surrender = new JRoundButton("投降");
        surrender.setPreferredSize(buttonSize);
        surrender.setMaximumSize(buttonSize);
        surrender.setMinimumSize(buttonSize);
        surrender.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/37.8)));
        surrender.setFocusPainted(false);
        surrender.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))!=0)surrender.setEnabled(false);

        // 添加按钮到控制面板
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/37.8)));
        add(backToArchive);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(musicOn);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(surrender);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(reset);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(playBack);
        add(Box.createVerticalGlue());

        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(!reset.isEnabled()) return;
                model.resetBoard();
                board.getPlayBackPanel().setVisible(false);;
                updateControlPanel();
                board.getStatusPanel().updateDisplay();
                revalidate();repaint();
                board.repaint();
            }
        });

        playBack.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(playBack.isEnabled())handleClickOnPlayBack(model);
            }
        });

        backToArchive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(backToArchive.isEnabled())returnToArchiveManager();
            }
        });

        musicOn.addMouseListener(new MouseAdapter() {

        });

        surrender.addMouseListener(new MouseAdapter() {
            // @Override
            // public void mouseClicked(MouseEvent e) {
            //     whoseTurnLabel.setText("黑方胜利！");
            //     whoseTurnLabel.setForeground(Color.BLACK);;
            // }
        });
    }

    public void resizeComponents() {
        setSize(board.getWindowWidth()/10, board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        
        Dimension buttonSize = new Dimension(board.getWindowWidth()/10, (int)(board.getWindowHeight()/(int)(board.getWindowHeight()/37.8)));
        
        JRoundButton[] buttons = {reset, playBack, backToArchive, musicOn, surrender};
        for (JRoundButton btn : buttons) {
            if (btn != null) {
                btn.setPreferredSize(buttonSize);
                btn.setMaximumSize(buttonSize);
                btn.setMinimumSize(buttonSize);
            }
        }
        
        removeAll();
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/37.8)));
        add(backToArchive);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(musicOn);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(surrender);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(reset);
        add(Box.createVerticalStrut((int)(board.getWindowHeight()/50.4)));
        add(playBack);
        add(Box.createVerticalGlue());
        
        revalidate();
        repaint();
    }

    public void updateControlPanel(){
        if((model.getType()&(1<<3))!=0 && (!model.getPlayBackOn())){
            reset.setEnabled(true);
            playBack.setEnabled(true);
            surrender.setEnabled(false);
        }else if((model.getType()&(1<<3))!=0 && model.getPlayBackOn()){
            reset.setEnabled(true);
            playBack.setEnabled(false);
            surrender.setEnabled(false);
        }else{
            if(model.getAllSteps().isEmpty()||(model.getType()&2)!=0)reset.setEnabled(false);
            else reset.setEnabled(true);
            playBack.setEnabled(false);
            surrender.setEnabled(true);
        }
        revalidate();
        repaint();
    }


    private void handleClickOnPlayBack(ChessBoardModel model){
        playBack.setEnabled(false);
        model.setPlayBackOn(true);
        board.getPlayBackPanel().resetIdx();
        board.getPlayBackPanel().setVisible(true);
        board.getPlayBackPanel().repaint();
        revalidate();repaint();board.repaint();
        board.getPlayBackPanel().getContentPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnPlayBackPanel(e.getX(), e.getY());
            }
        });
    }
    
    private void handleMouseClickOnPlayBackPanel(int x, int y){
        model.setSelectedIdx(y/board.getPlayBackPanel().getStepHeight());
        replacePieces(model.getSelectedIdx());
        board.getPlayBackPanel().getContentPanel().repaint();
    }

    private void replacePieces(int stepIdx){
        model.tryPlayBack(stepIdx);
        board.getChessBoardPanel().repaint();
    }

    private void returnToArchiveManager(){
        board.dispose();
    }
}
