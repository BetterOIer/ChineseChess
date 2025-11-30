package edu.sustech.xiangqi.ui;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
    private int peerPort;
    private boolean connected = false;
    
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

    // 可由外部设置棋盘模型/界面，以便收到远程指令时更新本地棋盘
    public void setChessBoardModel(ChessBoardModel model){
        this.chessBoardModel = model;
    }
    public void setChessBoard(ChessBoard cb){
        this.chessBoard = cb;
    }

    void handleMouseClick() throws IOException{
        // 解析目标端口（房间号），若为空或解析失败使用默认 PORT
        int targetPort = PORT;
        String roomText = room.getText();
        if(roomText != null && !roomText.trim().isEmpty()){
            try{
                targetPort = Integer.parseInt(roomText.trim());
            }catch(NumberFormatException ex){
                // 静默处理，使用默认端口
                targetPort = PORT;
            }
        }

        try {
            // 绑定随机本地端口，避免端口被占用导致失败
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            // 静默处理
            return;
        }
        running = true;
        connected = false;
        peerAddress = InetAddress.getByName("localhost");
        peerPort = targetPort;

        // 接收消息线程
        Thread receiveThread = new Thread(this::receiveMessages);
        receiveThread.start();
        
        // 发送消息线程（周期性发送 Handshake，连接后可以发送更新）
        Thread sendThread = new Thread(this::sendMessages);
        sendThread.start();
    }

    private void receiveMessages() {
        byte[] buffer = new byte[2048];
        
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String message = new String(packet.getData(), 0, packet.getLength());
                // 静默处理消息，解析协议
                try {
                    if (message != null) {
                        message = message.trim();
                        if (message.startsWith("Handshake")) {
                            // 对端发来握手：记录对端地址与端口，标记已连接
                            peerAddress = packet.getAddress();
                            peerPort = packet.getPort();
                            connected = true;
                        } else if (message.startsWith("MOVE")) {
                            // 协议：MOVE fromRow fromCol toRow toCol
                            String[] parts = message.split("\\s+");
                            if (parts.length >= 5) {
                                try {
                                    int fromRow = Integer.parseInt(parts[1]);
                                    int fromCol = Integer.parseInt(parts[2]);
                                    int toRow = Integer.parseInt(parts[3]);
                                    int toCol = Integer.parseInt(parts[4]);
                                    applyRemoteMove(fromRow, fromCol, toRow, toCol);
                                } catch (NumberFormatException nfe) {
                                    // 静默忽略
                                }
                            }
                        } else if (message.startsWith("UPDATE")) {
                            // 这里保留接收 UPDATE 的位置，项目可根据需求扩展
                            // 目前不对 UPDATE 做深解析（静默）
                        }
                    }
                } catch (Exception ex) {
                    // 静默处理解析/应用错误
                }
            } catch (IOException e) {
                // 静默处理接收错误；若运行标记为 false 则结束循环
                if (!running) break;
            }
        }
    }
    
    private void sendMessages() {
        try {
            // 发送循环：未连接时周期性发送 Handshake；连接后不自动 flood，外部可调用 sendMove/sendBoardUpdate
            while (running) {
                try {
                    if (!connected) {
                        String handshake = "Handshake " + user.getName();
                        byte[] data = handshake.getBytes();
                        DatagramPacket packet = new DatagramPacket(
                            data, data.length,
                            peerAddress, peerPort
                        );
                        try {
                            socket.send(packet);
                        } catch (IOException ignore) {
                            // 静默
                        }
                    }
                    Thread.sleep(1500); // 间隔发送，避免忙循环
                } catch (InterruptedException ie) {
                    // 静默处理并继续/退出
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            // 退出时确保 socket 被关闭（由 stop 管理）
        }
    }

    // 对外调用：发送一次移动指令给对端（协议为 MOVE）
    public void sendMove(int fromRow, int fromCol, int toRow, int toCol){
        if (socket == null || socket.isClosed() || peerAddress == null) return;
        String message = "MOVE " + fromRow + " " + fromCol + " " + toRow + " " + toCol;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, peerPort);
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
        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, peerPort);
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
    }
        
    public void stop() {
        running = false;
        connected = false;
        if (socket != null) {
            socket.close();
        }
    }
}
