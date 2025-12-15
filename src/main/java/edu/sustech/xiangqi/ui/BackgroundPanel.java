package edu.sustech.xiangqi.ui;

import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
    
    private Image backgroundImage;
    private ChessBoard board;
    
    public BackgroundPanel(ChessBoard board) {
        try{
            backgroundImage = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/ChessBoardBackground.png").getImage();
        }catch(Exception e){
            e.printStackTrace();
            backgroundImage = null;
        }
        this.board=board;
        setLayout(null);
        setSize(board.getWindowWidth(),board.getWindowHeight());
        setLocation(0,0);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }else{
            g.setColor(Style.defaultColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}