package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.ChessBoardModel;
import edu.sustech.xiangqi.model.AbstractPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoard extends JFrame {

    private final ChessBoardPanel chessBoardPanel;

    public ChessBoard(ChessBoardModel model){
        setTitle("中国象棋-"+model.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.chessBoardPanel =  new ChessBoardPanel(model);
        add(chessBoardPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
}

class ChessBoardPanel extends JPanel {
    private final ChessBoardModel model;
    private
    Image backgroundImage;

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
        // 加载背景图片
        try {
            backgroundImage = new ImageIcon("src/main/image/ChessBoardBackground.JPG").getImage();
        }
        catch (Exception e) {
            System.out.println("背景图片加载失败: " + e.getMessage());
            backgroundImage = null;
        }
        setPreferredSize(new Dimension(CELL_SIZE * (ChessBoardModel.getCols() - 1) + MARGIN * 2, CELL_SIZE * (ChessBoardModel.getRows() - 1) + MARGIN * 2));
        setOpaque(false);

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
        drawBackground(g2d);
        drawBoard(g2d);
        drawPieces(g2d);
    }

    private void drawBackground(Graphics2D g) {
        if (backgroundImage != null) {
            // 绘制背景图片，填充整个面板
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this
            );
        }
        else {
            // 如果背景图片加载失败，使用默认背景色
            g.setColor(new Color(220, 179, 92));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * 绘制棋盘
     */
    private void drawBoard(Graphics2D g) {
        g.setColor(Color.BLACK);
        // 设置棋盘外框线条粗细
        g.setStroke(new BasicStroke(2));
        // 绘制棋盘外框（粗线）
        int boardWidth = (ChessBoardModel.getCols() - 1) * CELL_SIZE;
        int boardHeight = (ChessBoardModel.getRows() - 1) * CELL_SIZE;
        // 设置外框与棋盘最外缘的间隔距离
        int borderMargin = 5;
        // 绘制外围边框 - 与棋盘最外缘保持间隔
        int borderX = MARGIN - borderMargin;
        int borderY = MARGIN - borderMargin;
        int borderWidth = boardWidth + borderMargin * 2;
        int borderHeight = boardHeight + borderMargin * 2;

        g.drawRect(borderX, borderY, borderWidth, borderHeight);
        // 设置内部线条粗细
        g.setStroke(new BasicStroke(1));

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

        // 绘制斜线
        drawDiagonalLines(g);

        //绘制棋子位置标记线
        drawPositionMarks(g);

        // 绘制“楚河”和“汉界”这两个文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("隶书", Font.BOLD, 30));

        int riverY = MARGIN + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGIN + CELL_SIZE * 2 - chuHeWidth / 2, riverY + 8);

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGIN + CELL_SIZE * 6 - hanJieWidth / 2, riverY + 8);
    }

    private void drawDiagonalLines(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));

        // 绘制红方九宫格斜线（上方）
        // 左上到右下的斜线
        g.drawLine(MARGIN + 3 * CELL_SIZE, MARGIN, MARGIN + 5 * CELL_SIZE, MARGIN + 2 * CELL_SIZE);
        // 右上到左下的斜线
        g.drawLine(MARGIN + 5 * CELL_SIZE, MARGIN, MARGIN + 3 * CELL_SIZE, MARGIN + 2 * CELL_SIZE);

        // 绘制黑方九宫格斜线（下方）
        // 左上到右下的斜线
        g.drawLine(MARGIN + 3 * CELL_SIZE, MARGIN + 7 * CELL_SIZE, MARGIN + 5 * CELL_SIZE, MARGIN + 9 * CELL_SIZE);
        // 右上到左下的斜线
        g.drawLine(MARGIN + 5 * CELL_SIZE, MARGIN + 7 * CELL_SIZE, MARGIN + 3 * CELL_SIZE, MARGIN + 9 * CELL_SIZE);

        // 绘制九宫格内部的交叉斜线（根据图2的详细样式）
        // 红方九宫格内部交叉线
        g.drawLine(MARGIN + 3 * CELL_SIZE, MARGIN + 1 * CELL_SIZE, MARGIN + 5 * CELL_SIZE, MARGIN + 1 * CELL_SIZE);
        g.drawLine(MARGIN + 4 * CELL_SIZE, MARGIN, MARGIN + 4 * CELL_SIZE, MARGIN + 2 * CELL_SIZE);

        // 黑方九宫格内部交叉线
        g.drawLine(MARGIN + 3 * CELL_SIZE, MARGIN + 8 * CELL_SIZE, MARGIN + 5 * CELL_SIZE, MARGIN + 8 * CELL_SIZE);
        g.drawLine(MARGIN + 4 * CELL_SIZE, MARGIN + 7 * CELL_SIZE, MARGIN + 4 * CELL_SIZE, MARGIN + 9 * CELL_SIZE);
    }

    /**
     *标记棋盘上炮位卒位
     */
    private void drawPositionMarks(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        int markLength = 6; // L形标记的长度

        // 炮位标记（双方各2个炮位）
        int[][] cannonPositions = {
                {2, 1}, {2, 7},  // 红方炮位（第3行，第2列和第8列）
                {7, 1}, {7, 7}   // 黑方炮位（第8行，第2列和第8列）
        };

        // 兵/卒位标记（双方各5个兵/卒位）
        int[][] soldierPositions = {
                {3, 0}, {3, 2}, {3, 4}, {3, 6}, {3, 8},  // 红方兵位（第4行）
                {6, 0}, {6, 2}, {6, 4}, {6, 6}, {6, 8}   // 黑方卒位（第7行）
        };

        // 绘制炮位标记（特殊的L形标记）
        for (int[] pos : cannonPositions) {
            int row = pos[0];
            int col = pos[1];
            drawCannonMarks(g, row, col, markLength);
        }

        // 绘制兵/卒位标记
        for (int[] pos : soldierPositions) {
            int row = pos[0];
            int col = pos[1];
            drawSoldierMarks(g, row, col, markLength);
        }
    }

    /**
     * 绘制炮位的特殊L形标记（四个方向的L形）
     */
    private void drawCannonMarks(Graphics2D g, int row, int col, int length) {
        int x = MARGIN + col * CELL_SIZE;
        int y = MARGIN + row * CELL_SIZE;

        // 炮位标记：在交叉点的四个角都绘制小L形
        if (row == 2) { // 红方炮位（棋盘上方）
            // 左上角L形
            g.drawLine(x - length, y, x, y);
            g.drawLine(x, y - length, x, y);
            // 右上角L形
            g.drawLine(x, y - length, x, y);
            g.drawLine(x, y, x + length, y);
            // 左下角L形
            g.drawLine(x - length, y, x, y);
            g.drawLine(x, y, x, y + length);
            // 右下角L形
            g.drawLine(x, y, x + length, y);
            g.drawLine(x, y, x, y + length);
        }
        else if (row == 7) { // 黑方炮位（棋盘下方）
            // 左上角L形
            g.drawLine(x - length, y, x, y);
            g.drawLine(x, y, x, y - length);
            // 右上角L形
            g.drawLine(x, y, x + length, y);
            g.drawLine(x, y, x, y - length);
            // 左下角L形
            g.drawLine(x - length, y, x, y);
            g.drawLine(x, y, x, y + length);
            // 右下角L形
            g.drawLine(x, y, x + length, y);
            g.drawLine(x, y, x, y + length);
        }
    }

    /**
     * 绘制兵/卒位的L形标记（指向棋盘中心的L形）
     */
    private void drawSoldierMarks(Graphics2D g, int row, int col, int length) {
        int x = MARGIN + col * CELL_SIZE;
        int y = MARGIN + row * CELL_SIZE;

        if (row == 3) { // 红方兵位
            // 兵位标记：L形指向棋盘中心（向下）
            if (col == 0) {
                // 最左边兵位：右下L形
                g.drawLine(x, y, x + length, y);
                g.drawLine(x, y, x, y + length);
            }
            else if (col == 8) {
                // 最右边兵位：左下L形
                g.drawLine(x, y, x - length, y);
                g.drawLine(x, y, x, y + length);
            }
            else {
                // 中间兵位：向下L形
                g.drawLine(x - length/
                                2, y, x + length/2
                        , y);
                g.drawLine(x, y, x, y + length);
            }
        }
        else if (row == 6) { // 黑方卒位
            // 卒位标记：L形指向棋盘中心（向上）
            if (col == 0) {
                // 最左边卒位：右上L形
                g.drawLine(x, y, x + length, y);
                g.drawLine(x, y, x, y - length);
            }
            else if (col == 8) {
                // 最右边卒位：左上L形
                g.drawLine(x, y, x - length, y);
                g.drawLine(x, y, x, y - length);
            }
            else {
                // 中间卒位：向上L形
                g.drawLine(x - length/
                                2, y, x + length/2
                        , y);
                g.drawLine(x, y, x, y - length);
            }
        }
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

            // 绘制circle
            g.setColor(new Color(245, 222, 179));
            g.fillOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

            // 绘制circle的灰色细线
            g.setColor(new Color(105, 105, 105));
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(x - PIECE_RADIUS + 5, y - PIECE_RADIUS + 5, PIECE_RADIUS * 2 - 10, PIECE_RADIUS * 2 - 10);
            // 绘制circle的黑色边框
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.8f));
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
            g.setFont(new Font("楷体", Font.BOLD, 32));
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
