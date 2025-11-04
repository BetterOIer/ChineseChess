package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.AbstractPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoardPanel extends JPanel {
    private final ChessBoardModel model;

    /**
     * 单个棋盘格子的尺寸（px）
     */
    private static final int CELL_SIZE = 64;

    /**
     * 棋盘边界与窗口边界的边距
     */
    private static final int MARGIN = 40;

    /**
     * 棋子的半径
     */
    private static final int PIECE_RADIUS = 25;

    private AbstractPiece selectedPiece = null;

    public ChessBoardPanel(ChessBoardModel model) {
        this.model = model;
        setPreferredSize(new Dimension(
                CELL_SIZE * (ChessBoardModel.getCols() - 1) + MARGIN * 2,
                CELL_SIZE * (ChessBoardModel.getRows() - 1) + MARGIN * 2
        ));
        setBackground(new Color(220, 179, 92));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void handleMouseClick(int x, int y) {
        int col = Math.round((float)(x - MARGIN) / CELL_SIZE);
        int row = Math.round((float)(y - MARGIN) / CELL_SIZE);

        if (!model.isValidPosition(row, col)) {
            return;
        }

        if (selectedPiece == null) {
            selectedPiece = model.getPieceAt(row, col);
        } else {
            model.movePiece(selectedPiece, row, col);
            selectedPiece = null;
        }

        // 处理完点击事件后，需要重新绘制ui界面才能让界面上的棋子“移动”起来
        // Swing 会将多个请求合并后再重新绘制，因此调用 repaint 后gui不会立刻变更
        // repaint 中会调用 paintComponent，从而重新绘制gui上棋子的位置等
        repaint();
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
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));

        // 绘制横线
        for (int i = 0; i < ChessBoardModel.getRows(); i++) {
            int y = MARGIN + i * CELL_SIZE;
            g.drawLine(MARGIN, y, MARGIN + (ChessBoardModel.getCols() - 1) * CELL_SIZE, y);
        }

        // 绘制竖线
        for (int i = 0; i < ChessBoardModel.getCols(); i++) {
            int x = MARGIN + i * CELL_SIZE;
            if (i == 0 || i == ChessBoardModel.getCols() - 1) {
                // 两边的竖线贯通整个棋盘
                g.drawLine(x, MARGIN, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            } else {
                // 中间的竖线分为上下两段（楚河汉界断开）
                g.drawLine(x, MARGIN, x, MARGIN + 4 * CELL_SIZE);
                g.drawLine(x, MARGIN + 5 * CELL_SIZE, x, MARGIN + (ChessBoardModel.getRows() - 1) * CELL_SIZE);
            }
        }

        // 绘制“楚河”和“汉界”这两个文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("楷体", Font.BOLD, 24));

        int riverY = MARGIN + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGIN + CELL_SIZE * 2 - chuHeWidth / 2, riverY + 8);

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGIN + CELL_SIZE * 6 - hanJieWidth / 2, riverY + 8);
    }

    /**
     * 绘制棋子
     */
    private void drawPieces(Graphics2D g) {
        // 遍历棋盘上的每一个棋子，每次循环绘制该棋子
        for (AbstractPiece piece : model.getPieces()) {
            // 计算每一个棋子的坐标
            int x = MARGIN + piece.getCol() * CELL_SIZE;
            int y = MARGIN + piece.getRow() * CELL_SIZE;

            boolean isSelected = (piece == selectedPiece);

            // 先绘制circle
            g.setColor(new Color(245, 222, 179));
            g.fillOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);
            // 绘制circle的黑色边框
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

            if (isSelected) {
                drawCornerBorders(g, x, y);
            }

            // 再在circle上面绘制对应的棋子名字
            if (piece.isRed()) {
                g.setColor(new Color(200, 0, 0));
            } else {
                g.setColor(Color.BLACK);
            }
            g.setFont(new Font("楷体", Font.BOLD, 22));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(piece.getName());
            int textHeight = fm.getAscent();
            g.drawString(piece.getName(), x - textWidth / 2, y + textHeight / 2 - 2);
        }
    }

    /**
     * 绘制选中棋子时的蓝色外边框效果
     */
    private void drawCornerBorders(Graphics2D g, int centerX, int centerY) {
        g.setColor(new Color(0, 100, 255));
        g.setStroke(new BasicStroke(3));

        int cornerSize = 32;
        int lineLength = 12;

        // 选中效果的边框实际上是8条line，每两个line组成一个角落的边框

        // 左上角的边框
        g.drawLine(centerX - cornerSize, centerY - cornerSize,
                centerX - cornerSize + lineLength, centerY - cornerSize);
        g.drawLine(centerX - cornerSize, centerY - cornerSize,
                centerX - cornerSize, centerY - cornerSize + lineLength);

        // 右上角的边框
        g.drawLine(centerX + cornerSize, centerY - cornerSize,
                centerX + cornerSize - lineLength, centerY - cornerSize);
        g.drawLine(centerX + cornerSize, centerY - cornerSize,
                centerX + cornerSize, centerY - cornerSize + lineLength);

        // 左下角的边框
        g.drawLine(centerX - cornerSize, centerY + cornerSize,
                centerX - cornerSize + lineLength, centerY + cornerSize);
        g.drawLine(centerX - cornerSize, centerY + cornerSize,
                centerX - cornerSize, centerY + cornerSize - lineLength);

        // 右下角的边框
        g.drawLine(centerX + cornerSize, centerY + cornerSize,
                centerX + cornerSize - lineLength, centerY + cornerSize);
        g.drawLine(centerX + cornerSize, centerY + cornerSize,
                centerX + cornerSize, centerY + cornerSize - lineLength);
    }
}
