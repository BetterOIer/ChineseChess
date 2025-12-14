package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StatusPanel extends JPanel{

    private ChessBoardModel model;
    private ChessBoard board;

    private JLabel redTurnLabel, blackTurnLabel, redWinLabel, blackWinLabel, bothWinLabel;

    public StatusPanel(ChessBoardModel model,ChessBoard board){

        this.model = model;
        this.board=board;

        setSize(board.getWindowWidth(), board.getWindowHeight()/13);
        setLocation(0,0);
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(Style.transprentColor);
        //TODO: 查验
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(111, 78, 55), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        Font font = new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/27));

        redTurnLabel = new JLabel("红方回合", SwingConstants.CENTER);
        redTurnLabel.setFont(font);
        redTurnLabel.setForeground(Color.RED);
        
        blackTurnLabel = new JLabel("黑方回合", SwingConstants.CENTER);
        blackTurnLabel.setFont(font);
        blackTurnLabel.setForeground(Color.BLACK);

        redWinLabel = new JLabel("红方胜利", SwingConstants.CENTER);
        redWinLabel.setFont(font);
        redWinLabel.setForeground(Color.RED);

        blackWinLabel = new JLabel("黑方胜利", SwingConstants.CENTER);
        blackWinLabel.setFont(font);
        blackWinLabel.setForeground(Color.BLACK);

        bothWinLabel = new JLabel("和棋", SwingConstants.CENTER);
        bothWinLabel.setFont(font);
        bothWinLabel.setForeground(Color.GREEN);
    }

    public void resizeComponents() {
        setSize(board.getWindowWidth(), board.getWindowHeight()/13);
        Font font = new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/27));
        redTurnLabel.setFont(font);blackTurnLabel.setFont(font);redWinLabel.setFont(font);blackWinLabel.setFont(font);bothWinLabel.setFont(font);
        setLocation(0,0);
        revalidate();
        repaint();
    }
    
    public void updateDisplay(){
        remove(redTurnLabel);
        remove(blackTurnLabel);
        remove(redWinLabel);
        remove(blackWinLabel);
        remove(bothWinLabel);
        if(false){//TODO:和棋判定
            add(bothWinLabel, BorderLayout.CENTER);
        }else if((model.getType()&8)!=0){
            if(model.getAllSteps().isEmpty()){
                if(model.getWhoseTurn())add(blackWinLabel, BorderLayout.CENTER);
                else add(redWinLabel, BorderLayout.CENTER);
            }else if(model.getAllSteps().getLast().getIsRed()){
                add(redWinLabel, BorderLayout.CENTER);
            }else{
                add(blackWinLabel, BorderLayout.CENTER);
            }
        }else{
            if(model.getWhoseTurn()){
                add(redTurnLabel, BorderLayout.CENTER);
            }else{
                add(blackTurnLabel, BorderLayout.CENTER);
            }
        }
        revalidate();
        repaint();
    }
}
