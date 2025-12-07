package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChessBoard extends JFrame {

    private ChessBoardModel model;

    private final ChessBoardPanel chessBoardPanel;
    private PlayBackPanel playBackPanel;
    private JButton reset, playBack;
    private Image backgroundImage;

    private boolean playBackOn= false;
    private int selectedIdx = -1;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenWidth = (int)(screenSize.width*0.7);
    static int screenHeight = (int)(screenSize.height*0.7);

    public ChessBoard(ChessBoardModel model){

        this.model=model;

        setTitle("中国象棋-"+model.getName());
        // 加载背景图片
        try {
            backgroundImage = new ImageIcon("src/main/image/ChessBoardBackground.png").getImage();
        }
        catch (Exception e) {
            System.out.println("背景图片加载失败: " + e.getMessage());
            backgroundImage = null;
        }

        // 设置自定义ContentPane以绘制背景
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(220, 179, 92));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);

        setSize(ChessBoardPanel.screenWidth, ChessBoardPanel.screenHeight);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        reset = new JButton("重置棋盘");
        reset.setLocation(10, 160);
        reset.setSize(100, 40);
        if((model.getType()&(1<<3))==0)reset.setVisible(false);
        add(reset);

        playBack = new JButton("复盘");
        playBack.setLocation(10,220);
        playBack.setSize(100,40);
        if((model.getType()&(1<<3))==0)playBack.setVisible(false);
        add(playBack);

        this.chessBoardPanel =  new ChessBoardPanel(model);
        add(chessBoardPanel);

        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                model.resetBoard();
                reset.setVisible(false);
                playBack.setVisible(false);
                playBackOn=false;
                chessBoardPanel.setPlayBackOn(false);
                if (playBackPanel != null) {
                    remove(playBackPanel);
                }
                repaint();
            }
        });

        playBack.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                handleClickOnPlayBack(model);
            }
        });

        chessBoardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if((model.getType()&(1<<3))!=0){
                    reset.setVisible(true);
                    if(!playBackOn)playBack.setVisible(true);
                }
            }
        });

    }
    
    public ChessBoardPanel getPanel(){
        return chessBoardPanel;
    }
    public JButton getReset(){
        return reset;
    }
    public JButton getPlayBack(){
        return playBack;
    }

    private void handleClickOnPlayBack(ChessBoardModel model){
        if (playBackPanel != null) {
            remove(playBackPanel);
        }
        playBackPanel = new PlayBackPanel(model.getTrueSteps());
        add(playBackPanel, 0);
        playBack.setVisible(false);
        playBackOn=true;
        chessBoardPanel.setPlayBackOn(true);
        playBackPanel.repaint();
        repaint();
        playBackPanel.getContentPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClickOnPlayBackPanel(e.getX(), e.getY());
            }
        });
    }
    
    private void handleMouseClickOnPlayBackPanel(int x, int y){
        selectedIdx = y / playBackPanel.getStepHeight();
        playBackPanel.setSelectedIdx(selectedIdx);
        chessBoardPanel.setSelectedIdx(selectedIdx);
        replacePieces(playBackPanel.getSelectedIdx());
        playBackPanel.getContentPanel().repaint();
    }

    private void replacePieces(int stepIdx){
        model.tryPlayBack(stepIdx);
        chessBoardPanel.repaint();
    }
}

class ChessBoardPanel extends JPanel {
    private final ChessBoardModel model;

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenWidth = (int)(screenSize.width*0.7);
    static int screenHeight = (int)(screenSize.height*0.7);
    private boolean playBackOn=false;
    private int selectedIdx=-1;
    /**
     * 单个棋盘格子的尺寸（px）
     */
    private static final int CELL_SIZE = 64;

    /**
     * 棋盘边界与窗口边界的边距
     */
    private static final int MARGIN = screenHeight / 7 ;

    /**
     * 棋子的半径
     */
    private static final int PIECE_RADIUS = 25;

    public void setPlayBackOn(boolean playBackOn){
        this.playBackOn=playBackOn;
    }

    private AbstractPiece selectedPiece = null;
    

    public ChessBoardPanel(ChessBoardModel model) {
        this.model = model;
        setSize(new Dimension(screenWidth, screenHeight));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public void setSelectedIdx(int selectedIdx){
        this.selectedIdx=selectedIdx;
    }

    public void handleMouseClick(int x, int y) {
        int col = Math.round((float)(x - MARGIN) / CELL_SIZE);
        int row = Math.round((float)(y - MARGIN) / CELL_SIZE);

        if (!model.isValidPosition(row, col)) {
            return;
        }else if(selectedPiece == null){
            selectedPiece= model.trySelectPiece(row, col);
        }else if(selectedPiece != null){
            if(model.getPieceAt(row, col)==null){
                model.tryMovePiece(row, col);
            }else{
                model.tryEatPiece(row, col);
            }
            selectedPiece=null;
            model.caneclSelection();
        }
        // 处理完点击事件后，需要重新绘制ui界面才能让界面上的棋子“移动”起来
        // Swing 会将多个请求合并后再重新绘制，因此调用 repaint 后gui不会立刻变更
        // repaint 中会调用 paintComponent，从而重新绘制gui上棋子的位置等
        repaint();
        /* System.out.println(model.getSteps()); */
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

        int rrY = MARGIN + 4 * CELL_SIZE + CELL_SIZE / 2;

        String chuHeText = "楚河";
        FontMetrics fm = g.getFontMetrics();
        int chuHeWidth = fm.stringWidth(chuHeText);
        g.drawString(chuHeText, MARGIN + CELL_SIZE * 2 - chuHeWidth / 2, rrY + 8);

        String hanJieText = "汉界";
        int hanJieWidth = fm.stringWidth(hanJieText);
        g.drawString(hanJieText, MARGIN + CELL_SIZE * 6 - hanJieWidth / 2, rrY + 8);
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
        int gap = 4; // 标记与交叉点的间距

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
        int x = MARGIN + col * CELL_SIZE;
        int y = MARGIN + row * CELL_SIZE;
        int gap = 4; // 标记与交叉点的间距

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
        if((!model.getAllSteps().isEmpty())&&(!playBackOn))drawPrePos(g, model.getAllSteps().getLast());
        else if((!model.getAllSteps().isEmpty())&&(playBackOn)&&this.selectedIdx==-1)drawPrePos(g, model.getTrueSteps().getLast());
        else if((!model.getAllSteps().isEmpty())&&(playBackOn))drawPrePos(g, model.getTrueSteps().get(selectedIdx));
        
        if(selectedPiece!=null)drawHitRange(g);
        // 遍历棋盘上的每一个棋子，每次循环绘制该棋子
        int redEaten=0,blackEaten=0;
        for (AbstractPiece piece : model.getPieces()) {
            // 计算每一个棋子的坐标
            int x = MARGIN + piece.getCol() * CELL_SIZE;
            int y = MARGIN + piece.getRow() * CELL_SIZE;

            if(!piece.getStatus()){
                if(piece.isRed()){
                    y=MARGIN+redEaten*(PIECE_RADIUS);
                    redEaten++;
                }else{
                    y=MARGIN+ChessBoardModel.getCols() * CELL_SIZE-blackEaten*(PIECE_RADIUS);
                    x = MARGIN + (ChessBoardModel.getRows()-1) * CELL_SIZE;
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

    private void drawHitRange(Graphics2D g){
        g.setColor(new Color(255, 128, 0));
        int r = 10;
        for(Coordinate coordinate:model.getMoveRange()){
            int centerY = MARGIN + coordinate.getRow()* CELL_SIZE;
            int centerX = MARGIN + coordinate.getCol()* CELL_SIZE;
            g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        }
        g.setColor(new Color(127, 0, 255));
        r=30;
        for(Coordinate coordinate:model.getEatRange()){
            int centerY = MARGIN + coordinate.getRow()* CELL_SIZE;
            int centerX = MARGIN + coordinate.getCol()* CELL_SIZE;
            g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        }
    }

    private void drawPrePos(Graphics2D g, Step step){
        g.setColor(new Color(0, 0, 0));
        int r = 10;
        int centerY = MARGIN + step.getFromRow()* CELL_SIZE;
        int centerX = MARGIN + step.getFromCol()* CELL_SIZE;
        g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
        g.setColor(new Color(120, 120, 120));
        r = 30;
        centerY = MARGIN + step.getToRow()* CELL_SIZE;
        centerX = MARGIN + step.getToCol()* CELL_SIZE;
        g.fillOval(centerX - r, centerY - r, r * 2, r * 2);
    }
}

class PlayBackPanel extends JScrollPane {

    private List<Step> steps;
    private int stepHeight = 40;
    private int selectedIdx = -1;

    private JPanel contentPanel;

    public JPanel getContentPanel(){
        return this.contentPanel;
    }

    public int getSelectedIdx(){
        return selectedIdx;
    }
    public void setSelectedIdx(int selectedIdx){
        this.selectedIdx = selectedIdx;
    }

    public int getStepHeight(){
        return this.stepHeight;
    }

    public PlayBackPanel(List<Step> steps) {
        this.steps = steps;
        this.selectedIdx = steps.size() - 1;

        // 创建内部面板用于绘制内容
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawSteps(g2d, getWidth());
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(280, steps.size() * stepHeight);
            }
        };
        
        contentPanel.setBackground(new Color(220, 179, 92));
        
        // 将内容面板设置为视口视图
        setViewportView(contentPanel);
        
        // 设置滚动条策略
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(16);

        // 设置在ChessBoard中的位置和大小
        // ChessBoard布局为null，必须手动设置bounds
        // 放置在右侧空白区域
        int x = ChessBoard.screenWidth - 320;
        int y = ChessBoard.screenHeight / 7;
        setBounds(x, y, 300, 400);

        
    }

    private void drawSteps(Graphics2D g, int width) {
        g.setStroke(new BasicStroke(2));

        for (int stepIdx = 0; stepIdx < steps.size(); stepIdx++) {
            Step stepNow = steps.get(stepIdx);

            int y = stepIdx * stepHeight;
            if (stepIdx == selectedIdx) g.setColor(new Color(200, 220, 255));
            else if (stepIdx % 2 == 0) g.setColor(Color.WHITE);
            else g.setColor(new Color(245, 245, 245));

            g.fillRect(0, y, width, stepHeight);

            // 绘制边框
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y, width, y);

            // 绘制标题
            g.setColor(Color.BLACK);
            g.setFont(new Font("SimHei", Font.BOLD, 20));
            // 调整文字y坐标使其居中 (y是顶部，加上偏移量)
            g.drawString((stepIdx+1)+" "+stepNow.getStepNameInCh(), 20, y + 28);

            // 绘制选中指示器
            if (stepIdx == selectedIdx) {
                g.setColor(new Color(0, 120, 215));
                g.fillRect(0, y, 5, stepHeight);
            }
        }

        // 绘制底部边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, steps.size() * stepHeight, width, steps.size() * stepHeight);
    }
}