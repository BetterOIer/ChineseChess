package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ChessBoardPanel extends JPanel {

    private ChessBoardModel model;
    private ChessBoard board;

    private  int CELL_SIZE;
    private  int MARGINX, MARGINY;
    private  int PIECE_RADIUS;

    private AbstractPiece selectedPiece = null;
    private java.util.function.BiConsumer<Integer, Integer> onLocalMove;

    public void setOnLocalMove(java.util.function.BiConsumer<Integer, Integer> onLocalMove) {
        this.onLocalMove = onLocalMove;
    }

    public ChessBoardPanel(ChessBoardModel model,ChessBoard board) {
        this.model = model;
        this.board = board;
        CELL_SIZE = board.getWindowHeight()/12;
        MARGINX = (board.getWindowHeight()/13*12-(ChessBoardModel.getCols() - 1) * CELL_SIZE)/2;
        MARGINY = (board.getWindowHeight()/13*12-(ChessBoardModel.getRows() - 1) * CELL_SIZE)/2;
        PIECE_RADIUS = board.getWindowHeight()/25;
        setSize(board.getWindowHeight()/13*12,board.getWindowHeight()/13*12);
        setLocation((board.getWindowWidth()-getSize().width)/2,board.getWindowHeight()/13);
        setOpaque(false);
        setBackground(Style.transprentColor);
        board.getStatusPanel().updateDisplay();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    /* chessBoardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if((model.getType()&(1<<3))!=0){
                    reset.setVisible(true);
                    if(!playBackOn)playBack.setVisible(true);
                }
            }
        }); */

    public void resizeComponents() {
        CELL_SIZE = board.getWindowHeight()/12;
        MARGINX = (board.getWindowHeight()/13*12-(ChessBoardModel.getCols() - 1) * CELL_SIZE)/2;
        MARGINY = (board.getWindowHeight()/13*12-(ChessBoardModel.getRows() - 1) * CELL_SIZE)/2;
        PIECE_RADIUS = board.getWindowHeight()/25;
        setSize(board.getWindowHeight()/13*12,board.getWindowHeight()/13*12);
        setLocation((board.getWindowWidth()-getSize().width)/2,board.getWindowHeight()/13);
        repaint();
    }

    public void handleMouseClick(int x, int y) {

        int col = Math.round((float)(x - MARGINX) / CELL_SIZE);
        int row = Math.round((float)(y - MARGINY) / CELL_SIZE);

        if (model.isValidPosition(row, col)) {
            if (onLocalMove != null) {
                onLocalMove.accept(row, col);
            }
            handleGridClick(row, col);
        }
    }

    public void handleGridClick(int row, int col) {
        if((model.getType()&2)!=0){
            // Check turn ownership
            User owner = model.getUserOwner();
            User red = model.getUserRed();
            User black = model.getUserBlack();
            boolean isLocalTurn = true;
            if (owner != null) {
                if (owner.getName().equals(red.getName())) {
                    if (!model.getWhoseTurn()) isLocalTurn = false;
                } else if (owner.equals(black)) {
                    if (model.getWhoseTurn()) isLocalTurn = false;
                }
            }
            
            if (!isLocalTurn) return;
        }
        if (!model.isValidPosition(row, col)) {
            return;
        }else if(selectedPiece == null){
            selectedPiece= model.trySelectPiece(row, col);
        }else if(selectedPiece != null){
            boolean moveSuccess = false;
            if(model.getPieceAt(row, col)==null){
                moveSuccess = model.tryMovePiece(row, col);
            }else{
                moveSuccess = model.tryEatPiece(row, col);
            }
            selectedPiece=null;
            model.caneclSelection();
            if(moveSuccess){
                board.getStatusPanel().updateDisplay();
                board.getControlPanel().updateControlPanel();
            }
        }
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
        // 设置棋盘外框线条粗细
        g.setStroke(new BasicStroke(board.getWindowHeight()/378));
        // 绘制棋盘外框（粗线）
        int boardWidth = (ChessBoardModel.getCols() - 1) * CELL_SIZE;
        int boardHeight = (ChessBoardModel.getRows() - 1) * CELL_SIZE;
        // 设置外框与棋盘最外缘的间隔距离
        int borderMargin = (int)(board.getWindowHeight()/151.2);
        // 绘制外围边框 - 与棋盘最外缘保持间隔
        int borderX = MARGINX - borderMargin;
        int borderY = MARGINY - borderMargin;
        int borderWidth = boardWidth + borderMargin * 2;
        int borderHeight = boardHeight + borderMargin * 2;

        g.drawRect(borderX, borderY, borderWidth, borderHeight);
        // 设置内部线条粗细
        g.setStroke(new BasicStroke(board.getWindowHeight()/756));

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

        //绘制棋子位置标记线
        drawPositionMarks(g);

        // 绘制“楚河”和“汉界”这两个文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("隶书", Font.BOLD, (int)(board.getWindowHeight()/25.2)));

        int rrY = MARGINY + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGINX + CELL_SIZE * 2 - chuHeWidth / 2, rrY + (int)(board.getWindowHeight()/94.5));

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGINX + CELL_SIZE * 6 - hanJieWidth / 2, rrY + (int)(board.getWindowHeight()/94.5));
    }

    private void drawDiagonalLines(Graphics2D g) {
        g.setColor(Color.BLACK);
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
        int x = MARGINX + col * CELL_SIZE;
        int y = MARGINY + row * CELL_SIZE;
        int gap = (int)(board.getWindowHeight()/189); // 标记与交叉点的间距

        // 左上角L形
        g.drawLine(x - gap - length, y - gap, x - gap, y - gap);
        g.drawLine(x - gap, y - gap - length, x - gap, y - gap);

        // 右上角L形
        g.drawLine(x + gap, y - gap, x + gap + length, y - gap);
        g.drawLine(x + gap, y - gap, x + gap, y - gap - length);

        // 左下角L形
        g.drawLine(x - gap - length, y + gap, x - gap, y + gap);
        g.drawLine(x - gap, y + gap, x - gap, y + gap + length);

        // 右下角L形
        g.drawLine(x + gap, y + gap, x + gap + length, y + gap);
        g.drawLine(x + gap, y + gap, x + gap, y + gap + length);
    }

    /**
     * 绘制兵/卒位的L形标记
     */
    private void drawSoldierMarks(Graphics2D g, int row, int col, int length) {
        int x = MARGINX + col * CELL_SIZE;
        int y = MARGINY + row * CELL_SIZE;
        int gap = (int)(board.getWindowHeight()/189); // 标记与交叉点的间距

        // 兵/卒位标记逻辑：除了最左和最右列只有两个角外，其余位置四个角都有标记

        if (col != 0) {
            // 绘制左边的标记
            // 左上角L形
            g.drawLine(x - gap - length, y - gap, x - gap, y - gap);
            g.drawLine(x - gap, y - gap - length, x - gap, y - gap);
            // 左下角L形
            g.drawLine(x - gap - length, y + gap, x - gap, y + gap);
            g.drawLine(x - gap, y + gap, x - gap, y + gap + length);
        }

        if (col != 8) {
            // 绘制右边的标记
            // 右上角L形
            g.drawLine(x + gap, y - gap, x + gap + length, y - gap);
            g.drawLine(x + gap, y - gap, x + gap, y - gap - length);
            // 右下角L形
            g.drawLine(x + gap, y + gap, x + gap + length, y + gap);
            g.drawLine(x + gap, y + gap, x + gap, y + gap + length);
        }
    }

    /**
     * 绘制棋子
     */
    private void drawPieces(Graphics2D g) {
        if((!model.getAllSteps().isEmpty())&&(!model.getPlayBackOn()))drawPrePos(g, model.getAllSteps().getLast());
        else if((!model.getAllSteps().isEmpty())&&(model.getPlayBackOn())&&model.getSelectedIdx()==-1)drawPrePos(g, model.getTrueSteps().getLast());
        else if((!model.getAllSteps().isEmpty())&&(model.getPlayBackOn()))drawPrePos(g, model.getTrueSteps().get(model.getSelectedIdx()));

        if(selectedPiece!=null)drawHitRange(g);
        // 遍历棋盘上的每一个棋子，每次循环绘制该棋子
        int redEaten=0,blackEaten=0;
        for (AbstractPiece piece : model.getPieces()) {
            // 计算每一个棋子的坐标
            int x = MARGINX + piece.getCol() * CELL_SIZE;
            int y = MARGINY + piece.getRow() * CELL_SIZE;

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

            boolean isSelected = (piece == selectedPiece);

            // 绘制circle
            g.setColor(new Color(245, 222, 179));
            g.fillOval(x - PIECE_RADIUS, y - PIECE_RADIUS, PIECE_RADIUS * 2, PIECE_RADIUS * 2);

            // 绘制circle的灰色/红色细线
            if (piece.isRed()) {
                g.setColor(new Color(200, 0, 0));
            } else {
                g.setColor(new Color(105, 105, 105));
            }
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(x - PIECE_RADIUS + (int)(board.getWindowHeight()/151.2), y - PIECE_RADIUS + (int)(board.getWindowHeight()/151.2), PIECE_RADIUS * 2 - (int)(board.getWindowHeight()/75.6), PIECE_RADIUS * 2 - (int)(board.getWindowHeight()/75.6));
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
            g.setFont(new Font("楷体", Font.BOLD,  (int)(board.getWindowHeight()/23.625)));
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
        g.setColor(new Color(104,184,142));
        g.setStroke(new BasicStroke(3));

        int cornerSize = (int)(board.getWindowHeight()/24);
        int lineLength = (int)(board.getWindowHeight()/63.75);

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

    private void drawHitRange(Graphics2D g){
        g.setColor(new Color(104,184,142,150));
        int r = (int)(board.getWindowHeight()/75.6);
        for(Coordinate coordinate:model.getMoveRange()){
            int centerY = MARGINY + coordinate.getRow()* CELL_SIZE;
            int centerX = MARGINX + coordinate.getCol()* CELL_SIZE;
            g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        }
        g.setColor(new Color(104,184,142));
        r=(int)(board.getWindowHeight()/21);
        for(Coordinate coordinate:model.getEatRange()){
            int centerY = MARGINY + coordinate.getRow()* CELL_SIZE;
            int centerX = MARGINX + coordinate.getCol()* CELL_SIZE;
            g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        }
    }

    private void drawPrePos(Graphics2D g, Step step){
        g.setColor(new Color(0, 0, 0,200));
        int r = (int)(board.getWindowHeight()/75.6);
        int centerY = MARGINY + step.getFromRow()* CELL_SIZE;
        int centerX = MARGINX + step.getFromCol()* CELL_SIZE;
        g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        g.setColor(new Color(120, 120, 120,200));
        r =(int)(board.getWindowHeight()/21);
        centerY = MARGINY + step.getToRow()* CELL_SIZE;
        centerX = MARGINX + step.getToCol()* CELL_SIZE;
        g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
    }
}