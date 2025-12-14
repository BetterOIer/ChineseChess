package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessBoardBirdView extends JPanel {

    private ChessBoardModel model;

    private  int CELL_SIZE;
    private  int MARGINX, MARGINY;
    private  int PIECE_RADIUS;
    private int spaceHeight,spaceWidth;

    public ChessBoardBirdView(ChessBoardModel model, int spaceWidth, int spaceHeight) {
        this.model = model;
        this.spaceHeight=spaceHeight;
        this.spaceWidth=spaceWidth;
        CELL_SIZE = spaceHeight/12;
        MARGINX = (spaceHeight/13*12-(ChessBoardModel.getCols() - 1) * CELL_SIZE)/2;
        MARGINY = (spaceHeight/13*12-(ChessBoardModel.getRows() - 1) * CELL_SIZE)/2;
        PIECE_RADIUS = spaceHeight/25;
        setSize(spaceHeight/13*12,spaceHeight/13*12);
        setOpaque(false);
        setBackground(Style.transprentColor);
    }

    private boolean isFlipped() {
        if ((model.getType() & 2) == 0) return false;
        User owner = model.getUserOwner();
        User black = model.getUserBlack();
        return owner != null && black != null && owner.getName().equals(black.getName());
    }

    private int getX(int col) {
        return MARGINX + (isFlipped() ? (8 - col) : col) * CELL_SIZE;
    }

    private int getY(int row) {
        return MARGINY + (isFlipped() ? (9 - row) : row) * CELL_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Demo的GUI都是由Swing中基本的组件组成的，比如背景的格子是用许多个line组合起来实现的，棋子是先绘制一个circle再在上面绘制一个text实现的
        // 因此绘制GUI的过程中需要自己手动计算每个组件的位置（坐标）
        drawBoard(g2d);
        drawPieces(g2d);
    }

    /**
     * 绘制棋盘
     */
    private void drawBoard(Graphics2D g) {
        g.setColor(new Color(180,180,180));
        // 设置内部线条粗细
        g.setStroke(new BasicStroke(Math.max(spaceHeight/756,1)));

        // 绘制横线
        for (int i = 0; i < ChessBoardModel.getRows(); i++) {
            int y = MARGINY + i * CELL_SIZE;
            g.drawLine(MARGINX, y, MARGINX + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
        }

        // 绘制竖线
        for (int i = 0; i < ChessBoardModel.getCols(); i++) {
            int x = MARGINX + i * CELL_SIZE;
            if (i == 0 || i == ChessBoardModel.getCols() - 1) {
                // 两边的竖线贯通整个棋盘
                g.drawLine(x, MARGINY, x, MARGINY + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            } else {
                // 中间的竖线分为上下两段（楚河汉界断开）
                g.drawLine(x, MARGINY, x, MARGINY + 4 * CELL_SIZE);
                g.drawLine(x, MARGINY + 5 * CELL_SIZE, x, MARGINY + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            }
        }
        // 绘制斜线
        drawDiagonalLines(g);
        // 绘制“楚河”和“汉界”这两个文字
        g.setColor(new Color(180,180,180));
        g.setFont(new Font("隶书", Font.BOLD, (int)(spaceHeight/25.2)));

        int rrY = MARGINY + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGINX + CELL_SIZE * 2 - chuHeWidth / 2, rrY + (int)(spaceHeight/94.5));

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGINX + CELL_SIZE * 6 - hanJieWidth / 2, rrY + (int)(spaceHeight/94.5));
    }

    private void drawDiagonalLines(Graphics2D g) {
        g.setColor(new Color(180,180,180));
        g.setStroke(new BasicStroke(1));

        // 绘制红方九宫格斜线（上方）
        // 左上到右下的斜线
        g.drawLine(MARGINX + 3 * CELL_SIZE, MARGINY, MARGINX + 5 * CELL_SIZE, MARGINY + 2 * CELL_SIZE);
        // 右上到左下的斜线
        g.drawLine(MARGINX + 5 * CELL_SIZE, MARGINY, MARGINX + 3 * CELL_SIZE, MARGINY + 2 * CELL_SIZE);

        // 绘制黑方九宫格斜线（下方）
        // 左上到右下的斜线
        g.drawLine(MARGINX + 3 * CELL_SIZE, MARGINY + 7 * CELL_SIZE, MARGINX + 5 * CELL_SIZE, MARGINY + 9 * CELL_SIZE);
        // 右上到左下的斜线
        g.drawLine(MARGINX + 5 * CELL_SIZE, MARGINY + 7 * CELL_SIZE, MARGINX + 3 * CELL_SIZE, MARGINY + 9 * CELL_SIZE);

        // 绘制九宫格内部的交叉斜线（根据图2的详细样式）
        // 红方九宫格内部交叉线
        g.drawLine(MARGINX + 3 * CELL_SIZE, MARGINY + 1 * CELL_SIZE, MARGINX + 5 * CELL_SIZE, MARGINY + 1 * CELL_SIZE);
        g.drawLine(MARGINX + 4 * CELL_SIZE, MARGINY, MARGINX + 4 * CELL_SIZE, MARGINY + 2 * CELL_SIZE);

        // 黑方九宫格内部交叉线
        g.drawLine(MARGINX + 3 * CELL_SIZE, MARGINY + 8 * CELL_SIZE, MARGINX + 5 * CELL_SIZE, MARGINY + 8 * CELL_SIZE);
        g.drawLine(MARGINX + 4 * CELL_SIZE, MARGINY + 7 * CELL_SIZE, MARGINX + 4 * CELL_SIZE, MARGINY + 9 * CELL_SIZE);
    }

    private void drawPieces(Graphics2D g) {
        int redEaten=0,blackEaten=0;
        for (AbstractPiece piece : model.getPieces()) {
            // 计算每一个棋子的坐标
            int x = getX(piece.getCol());
            int y = getY(piece.getRow());

            if(!piece.getStatus()){
                if(piece.isRed()){
                    y=MARGINY+redEaten*(PIECE_RADIUS);
                    redEaten++;
                }else{
                    y=MARGINY+ChessBoardModel.getCols() * CELL_SIZE-blackEaten*(PIECE_RADIUS);
                    x = MARGINX + (ChessBoardModel.getRows()-1) * CELL_SIZE;
                    blackEaten++;
                }
            }

            // 绘制circle的灰色/红色细线
            if (piece.isRed()) {
                g.setColor(new Color(200, 0, 0));
            } else {
                g.setColor(Color.BLACK);
            }
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);


            // 再在circle上面绘制对应的棋子名字
            if (piece.isRed()) {
                g.setColor(new Color(200, 0, 0));
            } else {
                g.setColor(Color.BLACK);
            }
            g.setFont(new Font("楷体", Font.BOLD,  (int)(spaceHeight/20)));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(piece.getName());
            int textHeight = fm.getAscent();
            g.drawString(piece.getName(), x - textWidth / 2, y + textHeight / 2 - 2);
        }
    }
}
