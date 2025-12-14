package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Connection extends JFrame {
    private JRoundTextField roomField;
    private JRoundButton confirmButton;
    private JLabel statusLabel;

    private static final int PORT = 1029;
    private DatagramSocket socket;
    private volatile boolean running = false;
    private volatile boolean connected = false;
    private ExecutorService executor;

    private String localUserName;
    private String roomNumber;
    private InetAddress peerAddress;
    private int peerPort;

    private ChessBoard chessBoard;
    private ChessBoardModel model;
    private WelcomePage welcomePage;

    public Connection() {
        setTitle("联机对战");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JLabel roomTip = new JLabel("请输入房间号：");
        roomTip.setLocation(50, 60);
        roomTip.setSize(120, 40);
        roomTip.setFont(Style.defaultFont);
        add(roomTip);

        roomField = new JRoundTextField();
        roomField.setLocation(170, 60);
        roomField.setSize(100, 40);
        add(roomField);

        confirmButton = new JRoundButton("确定");
        confirmButton.setLocation(150, 120);
        confirmButton.setSize(100, 40);
        confirmButton.setFont(Style.defaultFont);
        add(confirmButton);

        statusLabel = new JLabel("等待操作...", SwingConstants.CENTER);
        statusLabel.setLocation(50, 170);
        statusLabel.setSize(300, 30);
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel);

        confirmButton.addActionListener(e -> startConnection());
    }

    private void startConnection() {
        roomNumber = roomField.getText().trim();
        if (roomNumber.isEmpty()) {
            statusLabel.setText("房间号不能为空！");
            return;
        }

        try {
            User user = DBOperationUser.getUserInUse();
            if (user == null) {
                statusLabel.setText("未登录用户！");
                return;
            }
            localUserName = user.getName();
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("数据库错误！");
            return;
        }

        confirmButton.setEnabled(false);
        roomField.setEnabled(false);
        statusLabel.setText("正在寻找对手...");
        
        running = true;
        executor = Executors.newCachedThreadPool();
        
        try {
            // 尝试绑定固定端口，如果失败则使用随机端口
            try {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));
            } catch (SocketException e) {
                socket = new DatagramSocket();
            }
            socket.setBroadcast(true);
        } catch (SocketException ex) {
            ex.printStackTrace();
            statusLabel.setText("网络初始化失败");
            return;
        }

        executor.submit(this::receiveLoop);
        executor.submit(this::broadcastLoop);
    }

    private void broadcastLoop() {
        while (running && !connected) {
            try {
                JSONObject json = new JSONObject();
                json.put("type", "DISCOVER");
                json.put("room", roomNumber);
                json.put("user", localUserName);
                
                byte[] data = json.toString().getBytes();
                // 广播到 255.255.255.255:1029
                DatagramPacket packet = new DatagramPacket(data, data.length, 
                    InetAddress.getByName("255.255.255.255"), PORT);
                socket.send(packet);
                
                Thread.sleep(1000);
            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private void receiveLoop() {
        byte[] buffer = new byte[4096];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                handleMessage(msg, packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private void handleMessage(String msg, InetAddress address, int port) {
        try {
            JSONObject json = new JSONObject(msg);
            String type = json.optString("type");
            String room = json.optString("room");
            
            if ("DISCOVER".equals(type)) {
                if (connected) return;
                if (!roomNumber.equals(room)) return;
                
                String remoteUser = json.optString("user");
                if (localUserName.equals(remoteUser)) return; // 忽略自己

                // 发现对手
                peerAddress = address;
                peerPort = port;

                // 简单的握手逻辑：用户名大的作为红方（发起方）
                if (localUserName.compareTo(remoteUser) > 0) {
                    sendStart(localUserName, remoteUser);
                    initGame(localUserName, remoteUser);
                }
                // 如果用户名小，则等待对方发送START
                
            } else if ("START".equals(type)) {
                if (connected) return;
                String redName = json.optString("red");
                String blackName = json.optString("black");
                
                // 确认是发给我的
                if (localUserName.equals(redName) || localUserName.equals(blackName)) {
                    peerAddress = address;
                    peerPort = port;
                    initGame(redName, blackName);
                }
                
            } else if ("MOVE".equals(type)) {
                if (!connected) return;
                int row = json.getInt("row");
                int col = json.getInt("col");
                
                SwingUtilities.invokeLater(() -> {
                    if (chessBoard != null) {
                        chessBoard.getChessBoardPanel().handleGridClick(row, col);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendStart(String redName, String blackName) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "START");
            json.put("red", redName);
            json.put("black", blackName);
            sendPacket(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMove(int row, int col) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "MOVE");
            json.put("row", row);
            json.put("col", col);
            sendPacket(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPacket(String msg) throws IOException {
        if (peerAddress == null) return;
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, peerPort);
        socket.send(packet);
    }

    private void initGame(String redName, String blackName) {
        connected = true;
        SwingUtilities.invokeLater(() -> {
            try {
                statusLabel.setText("连接成功！正在进入游戏...");
                
                // 确保用户存在于数据库
                User redUser = DBOperationUser.getUserByName(redName);
                if (redUser == null) {
                    redUser = new User(DBOperationUser.getUserCount(), redName, null, 2);
                    DBOperationUser.insertUser(redUser);
                    redUser = DBOperationUser.getUserByName(redName);
                }
                
                User blackUser = DBOperationUser.getUserByName(blackName);
                if (blackUser == null) {
                    blackUser = new User(DBOperationUser.getUserCount(), blackName, null, 2);
                    DBOperationUser.insertUser(blackUser);
                    blackUser = DBOperationUser.getUserByName(blackName);
                }

                User currentUser = DBOperationUser.getUserInUse();
                
                // 创建棋盘模型
                model = new ChessBoardModel(
                    DBOperationBoard.getBoardCount(), 
                    2, // 联机模式
                    redUser, 
                    blackUser, 
                    currentUser, 
                    true // 红方先手
                );
                
                DBOperationBoard.insertBoard(model);
                
                chessBoard = new ChessBoard(model, welcomePage );
                // 设置本地移动监听，发送给对方
                chessBoard.getChessBoardPanel().setOnLocalMove(this::sendMove);
                chessBoard.setVisible(true);
                
                // 关闭连接窗口
                setVisible(false);
                
            } catch (SQLException e) {
                e.printStackTrace();
                statusLabel.setText("游戏初始化失败");
                connected = false;
            }
        });
    }
    
    public void stop() {
        running = false;
        connected = false;
        if (socket != null) {
            socket.close();
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
