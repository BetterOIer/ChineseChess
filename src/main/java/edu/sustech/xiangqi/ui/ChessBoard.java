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
    private JRoundButton reset, playBack, backToArchive, musicOn, surrender;
    private Image backgroundImage;

    private boolean playBackOn= false;
    private int selectedIdx = -1;

    private JLabel whoseTurnLabel;


    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int screenWidth = (int)(screenSize.width*0.7);
    static int screenHeight = (int)(screenSize.height*0.7);

    public ChessBoard(ChessBoardModel model){

        this.model=model;

        setTitle("中国象棋-"+model.getName());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 加载背景图片
        try {
            backgroundImage = new ImageIcon("src/main/java/edu/sustech/xiangqi/assets/images/ChessBoardBackground.png").getImage();
        }
        catch (Exception e) {
            System.out.println("背景图片加载失败: " + e.getMessage());
            backgroundImage = null;
        }

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // 创建顶部状态栏
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusBar.setPreferredSize(new Dimension(ChessBoardPanel.screenWidth, 60));
        statusBar.setBackground(new Color(0, 0, 0,0));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(111, 78, 55), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        whoseTurnLabel = new JLabel("红方回合", SwingConstants.CENTER);
        whoseTurnLabel.setFont(new Font("隶书", Font.BOLD, 28));
        whoseTurnLabel.setForeground(new Color(200, 0, 0));
        statusBar.add(whoseTurnLabel);

        // 创建棋盘面板
        this.chessBoardPanel = new ChessBoardPanel(model);

        // 创建右侧控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(193, 154, 107, 200));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        controlPanel.setPreferredSize(new Dimension(150, ChessBoardPanel.screenHeight));

        // 创建按钮
        reset = new JRoundButton("重置棋盘");
        reset.setPreferredSize(new Dimension(120, 40));
        reset.setMaximumSize(new Dimension(120, 40));
        reset.setFont(new Font("隶书", Font.BOLD, 16));
        reset.setFocusPainted(false);
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))==0)reset.setVisible(false);

        playBack = new JRoundButton("复盘");
        playBack.setPreferredSize(new Dimension(120, 40));
        playBack.setMaximumSize(new Dimension(120, 40));
        playBack.setFont(new Font("隶书", Font.BOLD, 16));
        playBack.setFocusPainted(false);
        playBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        if((model.getType()&(1<<3))==0)playBack.setVisible(false);
        backToArchive = new JRoundButton("返回存档");
        backToArchive.setPreferredSize(new Dimension(120, 40));
        backToArchive.setMaximumSize(new Dimension(120, 40));
        backToArchive.setFont(new Font("隶书", Font.BOLD, 16));
        backToArchive.setFocusPainted(false);
        backToArchive.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicOn = new JRoundButton("音乐");
        musicOn.setPreferredSize(new Dimension(120, 40));
        musicOn.setMaximumSize(new Dimension(120, 40));
        musicOn.setFont(new Font("隶书", Font.BOLD, 16));
        musicOn.setFocusPainted(false);
        musicOn.setAlignmentX(Component.CENTER_ALIGNMENT);
        surrender = new JRoundButton("投降");
        surrender.setPreferredSize(new Dimension(120, 40));
        surrender.setMaximumSize(new Dimension(120, 40));
        surrender.setFont(new Font("隶书", Font.BOLD, 16));
        surrender.setFocusPainted(false);
        surrender.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 添加按钮到控制面板
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(reset);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(backToArchive);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(musicOn);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(surrender);
        controlPanel.add(Box.createVerticalStrut(15));
        playBack.setVisible(false);  // 初始状态下复盘按钮不可见
        controlPanel.add(playBack);
        controlPanel.add(Box.createVerticalGlue());  // 空白填充

        // 创建中心区域
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(chessBoardPanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.EAST);

        //将组件添加到主面板
        mainPanel.add(statusBar, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 创建背景面板并设置为主容器
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);

        //设置窗口尺寸
        setSize(ChessBoardPanel.screenWidth + 150, ChessBoardPanel.screenHeight + 60);
        setLocationRelativeTo(null);

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

        backToArchive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                returnToArchiveManager();
            }
        });

        musicOn.addMouseListener(new MouseAdapter() {

        });

        surrender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                whoseTurnLabel.setText("黑方胜利！");
                whoseTurnLabel.setForeground(Color.BLACK);;
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

    // 背景图片的面板
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
            setLayout(new BorderLayout());
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // 绘制背景图片
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // 如果没有背景图片，使用默认颜色
                g.setColor(new Color(220, 179, 92));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * 更新回合显示
     */
    public void updateTurnDisplay() {
        String currentPlayer = model.getCurrentPlayer();
        String gameResult = model.getGameResult();
        SwingUtilities.invokeLater(() -> {
            if (gameResult != null && !gameResult.isEmpty()) {
                // 游戏结束，显示结果
                switch(gameResult) {
                    case "RED_WIN":
                        whoseTurnLabel.setText("红方胜利！");
                        whoseTurnLabel.setForeground(new Color(200, 0, 0)); // 红色
                        break;
                    case "BLACK_WIN":
                        whoseTurnLabel.setText("黑方胜利！");
                        whoseTurnLabel.setForeground(Color.BLACK); // 黑色
                        break;
                    case "DRAW":
                        whoseTurnLabel.setText("和棋！");
                        whoseTurnLabel.setForeground(new Color(0, 100, 0)); // 绿色
                        break;
                    default:
                        updateTurnByCurrentPlayer(currentPlayer);
                }
            } else {
                // 游戏进行中，显示当前回合
                updateTurnByCurrentPlayer(currentPlayer);
            }
        });
    }
    /**
     * 根据当前玩家更新回合显示
     */
    private void updateTurnByCurrentPlayer(String currentPlayer) {
        if ("RED".equals(currentPlayer)) {
            whoseTurnLabel.setText("红方回合");
            whoseTurnLabel.setForeground(new Color(200, 0, 0)); // 红色
        } else if ("BLACK".equals(currentPlayer)) {
            whoseTurnLabel.setText("黑方回合");
            whoseTurnLabel.setForeground(Color.BLACK); // 黑色
        }
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

    private void returnToArchiveManager(){
        try {
            // 获取当前用户的存档列表
            User currentUser = DBOperationUser.getUserInUse();
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this,
                        "没有找到登录用户，请重新登录",
                        "提示",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            List<ChessBoardModel> archives = DBOperationBoard.getBoardsByUser(currentUser);
            ArchiveManager archiveManager = new ArchiveManager(archives);
            archiveManager.setVisible(true);
            this.dispose();  // 关闭当前棋盘窗口
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "无法返回到存档管理器: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
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
    private static final int CELL_SIZE = screenHeight / 12;

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
        setBackground(new Color(0, 0, 0, 0));

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
            boolean moveSuccess = false;
            if(model.getPieceAt(row, col)==null){
                model.tryMovePiece(row, col);
                moveSuccess = true;
            }else{
                model.tryEatPiece(row, col);
                moveSuccess = true;
            }
            selectedPiece=null;
            model.caneclSelection();

            if
            (moveSuccess) {
                updateParentTurnDisplay();
            }
        }
        // 处理完点击事件后，需要重新绘制ui界面才能让界面上的棋子“移动”起来
        // Swing 会将多个请求合并后再重新绘制，因此调用 repaint 后gui不会立刻变更
        // repaint 中会调用 paintComponent，从而重新绘制gui上棋子的位置等
        repaint();
        /* System.out.println(model.getSteps()); */
    }

    private void updateParentTurnDisplay() {
        // 获取父窗口（ChessBoard）
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof ChessBoard)) {
            parent = parent.getParent();
        }
        if (parent instanceof ChessBoard) {
            ((ChessBoard) parent).updateTurnDisplay();
        }
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
            g.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 20));
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