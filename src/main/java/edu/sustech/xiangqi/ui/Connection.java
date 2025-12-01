package edu.sustech.xiangqi.ui;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import org.json.JSONObject;

import edu.sustech.xiangqi.model.*;

public class Connection extends JFrame{
    private User user;
    private ChessBoardModel chessBoardModel;
    private ChessBoard chessBoard;

    private JTextField room;
    private JButton join;
    private JLabel waitInfo;

    private static final int PORT = 1029;
    private DatagramSocket socket;
    private boolean running;

    // 记录对端信息
    private InetAddress peerAddress;
    private boolean connected = false;
    private boolean confirm = false;
    private String candidate = null;
    
    public Connection(User user){
        setTitle("等待连接");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,200);
        setLocationRelativeTo(null);

        this.user = user;

        
        JLabel roomTip = new JLabel("创建或加入房间：");
        roomTip.setLocation(10, 60);
        roomTip.setSize(120,40);
        add(roomTip);

        room = new JTextField();
        room.setLocation(130, 60);
        room.setSize(40, 40);
        add(room);

        join = new JButton("加入");
        join.setLocation(190, 60);
        join.setSize(60, 40);
        add(join);

        waitInfo = new JLabel();
        waitInfo.setLocation(10, 120);
        waitInfo.setSize(120,40);
        add(waitInfo);

        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    handleMouseClick();
                }catch(IOException e2){
                    // 静默处理
                }
            }
        });
    }

    void handleMouseClick() throws IOException{
        try{
            socket = new DatagramSocket(PORT);
        }catch(SocketException ex){
            ex.printStackTrace();
        }
        running = true;
        connected = false;
        peerAddress = InetAddress.getByName("localhost");
        /* PORT = PORT; */

        // 接收消息线程
        Thread receiveThread = new Thread(this::receiveMessages);
        receiveThread.start();
        
        // 发送消息线程（周期性发送 Handshake，连接后可以发送更新）
        Thread sendThread = new Thread(this::sendMessages);
        sendThread.start();
    }

    private void sendMessages(){
        while(running){
            try{
                if ((!connected)&&(!confirm)){
                    String handshake = encodeBuff("Handshake",user,room.getText());
                    byte[] data = handshake.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
                    try{
                        socket.send(packet);
                        System.out.println("sent:"+handshake);
                    }catch(IOException ignore){
                        ignore.printStackTrace();
                    }
                    Thread.sleep(1000); // 间隔发送，避免忙循环
                }else if((!connected)&&confirm){
                    String confirm = encodeBuff("Confirm",user,room.getText());
                    byte[] data = confirm.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
                    try{
                        socket.send(packet);
                        System.out.println("sent:"+confirm);
                    }catch(IOException ignore){
                        ignore.printStackTrace();
                    }
                    Thread.sleep(1000); // 间隔发送，避免忙循环
                }else{
                    Thread.sleep(1000);
                }
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    private void receiveMessages() {
        byte[] buffer = new byte[2048];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("receive:"+message);
                decodeBuff(message);
            }catch(IOException e){
                if (!running) break;
            }
        }
    }

    private String encodeBuff(String aim, User user, String msg){
        JSONObject json = new JSONObject();
        json.put("aim", aim);
        json.put("user", user.toString());
        json.put("msg", msg);
        return json.toString();
    }
private void decodeBuff(String msg) {
        if (msg==null||msg.trim().isEmpty()) {
            return;
        }
        JSONObject json = new JSONObject(msg);
        
        String aim = json.optString("aim", "");
        String userStr = json.optString("user", "");
        String message = json.optString("msg", "");

        if(userStr==user.toString()) return;

        if(aim=="Handshake"){
            if((!this.connected) && (!this.confirm)){
                if(message==room.getText()){
                    this.confirm=true;
                    this.candidate=userStr;
                }
            }
        }
        if(aim=="Confirm"){
            if((!this.connected) && (this.confirm)){
                if(this.candidate==userStr){
                    try{
                        DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(),userStr,null));
                        chessBoardModel = new ChessBoardModel(DBOperationBoard.getBoardCount(), 2, user, DBOperationUser.getUserByName(userStr), true);
                        chessBoard = new ChessBoard(chessBoardModel);
                        this.connected=true;
                        chessBoard.setVisible(true);
                        setVisible(false);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        
}
    
/* 
    // 可由外部设置棋盘模型/界面，以便收到远程指令时更新本地棋盘
    public void setChessBoardModel(ChessBoardModel model){
        this.chessBoardModel = model;
    }
    public void setChessBoard(ChessBoard cb){
        this.chessBoard = cb;
    }

    

    
    
    

    // 对外调用：发送一次移动指令给对端（协议为 MOVE）
    public void sendMove(int fromRow, int fromCol, int toRow, int toCol){
        if (socket == null || socket.isClosed() || peerAddress == null) return;
        String message = "MOVE " + fromRow + " " + fromCol + " " + toRow + " " + toCol;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            // 静默处理
        }
    }

    // 对外调用：发送棋盘更新（应用可自定义 UPDATE 内容）
    public void sendBoardUpdate(String payload){
        if (socket == null || socket.isClosed() || peerAddress == null) return;
        String message = "UPDATE " + payload;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            // 静默处理
        }
    }

    // 简单地在本地应用远端发来的移动（安静失败，不抛出）
    private void applyRemoteMove(int fromRow, int fromCol, int toRow, int toCol){
        try {
            if (this.chessBoardModel == null) return;
            AbstractPiece piece = this.chessBoardModel.getPieceAt(fromRow, fromCol);
            if (piece == null) return;
            AbstractPiece target = this.chessBoardModel.getPieceAt(toRow, toCol);
            if (target == null) {
                // 普通移动
                Step nowStep = new Step(piece.getType(), piece.getRow(), piece.getCol(), toRow, toCol, 0);
                this.chessBoardModel.updateBoards(nowStep);
                piece.moveTo(toRow, toCol);
            } else {
                // 吃子
                Step step1 = new Step(target.getType(), target.getRow(), target.getCol(), -1, -1, 1);
                target.setStatus(false);
                target.moveTo(-1, -1);
                this.chessBoardModel.updateBoards(step1);
                Step step2 = new Step(piece.getType(), piece.getRow(), piece.getCol(), toRow, toCol, 0);
                this.chessBoardModel.updateBoards(step2);
                piece.moveTo(toRow, toCol);
                if (target.getType() == 7) {
                    // 将被吃，标记已结束
                    int bt = this.chessBoardModel.getType() | 8;
                    this.chessBoardModel.setType(bt);
                    try {
                        DBOperationBoard.updateBoardType(this.chessBoardModel.getId(), bt);
                    } catch (SQLException ex) {
                        // 静默处理
                    }
                }
            }
            // 切换回合、更新时间并持久化（静默处理异常）
            this.chessBoardModel.setLastModTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            try{
                DBOperationBoard.updateBoardNowStatus(this.chessBoardModel.getId(), this.chessBoardModel.getPieces());
                DBOperationBoard.updateBoardHistory(this.chessBoardModel.getId(), this.chessBoardModel.getSteps());
                DBOperationBoard.updateBoardDate(this.chessBoardModel.getId(), this.chessBoardModel.getLastModTime());
                DBOperationBoard.updateBoardWhoseTurn(this.chessBoardModel.getId(), this.chessBoardModel.getWhoseTurn());
            }catch(SQLException ex){
                // 静默处理
            }
            // 触发界面刷新（若有界面）
            if (this.chessBoard != null) {
                SwingUtilities.invokeLater(() -> {
                    this.chessBoard.repaint();
                });
            }
        } catch (Exception ex) {
            // 静默处理任何意外
        }
    } */
        
    public void stop() {
        running = false;
        connected = false;
        if (socket != null) {
            socket.close();
        }
    }
}
