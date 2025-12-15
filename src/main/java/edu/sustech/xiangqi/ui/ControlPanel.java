package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ControlPanel extends JPanel{

    private ChessBoardModel model;
    private ChessBoard board;

    private JButton reset, playBack, backToArchive, musicOn, surrender;

    private CreateTransparentButton transparentButton = new CreateTransparentButton();


    public ControlPanel(ChessBoardModel model,ChessBoard board){

        this.model=model;
        this.board=board;

        setSize(Math.max(150,board.getWindowWidth()/8), board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBackground(Style.defaultColor);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        Dimension buttonSize = new Dimension(Math.max(150,board.getWindowWidth()/8), (int)(board.getWindowHeight()/20));

        reset = transparentButton.createTransparentButton("重置棋盘", Math.max(150,board.getWindowWidth()/8),(int)(board.getWindowHeight()/20));
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        reset.setPreferredSize(buttonSize);
        add(reset);
        if((model.getType()&8)==0)reset.setEnabled(false);
        if((model.getType()&2)!=0)reset.setEnabled(false);

        playBack = transparentButton.createTransparentButton("复盘", Math.max(150,board.getWindowWidth()/8),(int)(board.getWindowHeight()/20));
        playBack.setPreferredSize(buttonSize);
        playBack.setMaximumSize(buttonSize);
        playBack.setMinimumSize(buttonSize);
        playBack.setFont(new Font("隶书", Font.BOLD, 16));
        playBack.setFocusPainted(false);
        playBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))==0)playBack.setEnabled(false);

        backToArchive = transparentButton.createTransparentButton("返回存档", Math.max(150,board.getWindowWidth()/8),(int)(board.getWindowHeight()/20));
        backToArchive.setPreferredSize(buttonSize);
        backToArchive.setMaximumSize(buttonSize);
        backToArchive.setMinimumSize(buttonSize);
        backToArchive.setFont(new Font("隶书", Font.BOLD, 16));
        backToArchive.setFocusPainted(false);
        backToArchive.setAlignmentX(Component.CENTER_ALIGNMENT);

        musicOn = transparentButton.createTransparentButton("音效开", Math.max(150,board.getWindowWidth()/8),(int)(board.getWindowHeight()/20));
        musicOn.setPreferredSize(buttonSize);
        musicOn.setMaximumSize(buttonSize);
        musicOn.setMinimumSize(buttonSize);
        musicOn.setFont(new Font("隶书", Font.BOLD, 16));
        musicOn.setFocusPainted(false);
        musicOn.setAlignmentX(Component.CENTER_ALIGNMENT);

        surrender = transparentButton.createTransparentButton("投降", Math.max(150,board.getWindowWidth()/8),(int)(board.getWindowHeight()/20));
        surrender.setPreferredSize(buttonSize);
        surrender.setMaximumSize(buttonSize);
        surrender.setMinimumSize(buttonSize);
        surrender.setFont(new Font("隶书", Font.BOLD, 16));
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
            @Override
            public void mouseClicked(MouseEvent e) {
                if(model.getMusicOn()){
                    musicOn.setText("音效关");
                }else{
                    musicOn.setText("音效开");
                }
                model.setMusicOn(!model.getMusicOn());
                updateControlPanel();
            }
        });

        surrender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSurrender(); // 调用投降处理方法
            }
        });
        updateControlPanel();
    }

    public void resizeComponents() {
        setSize(Math.max(150,board.getWindowWidth()/8), board.getWindowHeight()/13*12);
        setLocation(0,board.getWindowHeight()/13);
        
        Dimension buttonSize = new Dimension(Math.max(150,board.getWindowWidth()/8), (int)(board.getWindowHeight()/(int)(board.getWindowHeight()/37.8)));
        
        JButton[] buttons = {reset, playBack, backToArchive, musicOn, surrender};
        for (JButton btn : buttons) {
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
            if((model.getType()&2)!=0){
                if(model.getWhoseTurn()){
                    if(!model.getUserRed().getName().equals(model.getUserOwner().getName())){
                        surrender.setEnabled(false);
                    }
                }else{
                    if(!model.getUserBlack().getName().equals(model.getUserOwner().getName())){
                        surrender.setEnabled(false);
                    }
                }
            }
        }
        if((model.getType()&2)!=0)reset.setEnabled(false);
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

    private void handleSurrender() {
        // 1. 判定胜负（当前回合方投降，对方胜利）
        model.setType((model.getType()|8));
        try{
            DBOperationBoard.updateBoardType(model.getId(), model.getType());
        }catch(SQLException e){
            e.printStackTrace();
        }
        board.getStatusPanel().updateDisplay();
        updateControlPanel();
        revalidate();repaint();
        board.getChessBoardPanel().repaint();
    }

}