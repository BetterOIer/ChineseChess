package edu.sustech.xiangqi.ui;

import edu.sustech.xiangqi.model.*;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Connection extends JFrame {
    private JRoundTextField roomField;
    private JRoundButton confirmButton;
    private JRoundButton cancelButton;
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

    public Connection() {
        setTitle("联机对战");
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                stop();
                new WelcomePage(false).setVisible(true);
            }
        });

        initUI();
    }

    private void initUI() {
        JLabel roomTip = new JLabel("请输入房间号：");
        roomTip.setLocation(50, 60);
        roomTip.setSize(180, 40);
        roomTip.setFont(new Font("隶书", Font.PLAIN, 20));;
        add(roomTip);

        roomField = new JRoundTextField();
        roomField.setLocation(230, 60);
        roomField.setSize(100, 40);
        add(roomField);

        confirmButton = new JRoundButton("确定");
        confirmButton.setLocation(90, 120);
        confirmButton.setSize(100, 40);
        confirmButton.setFont(new Font("隶书", Font.PLAIN, 20));
        add(confirmButton);

        cancelButton = new JRoundButton("取消");
        cancelButton.setLocation(210, 120);
        cancelButton.setSize(100, 40);
        cancelButton.setFont(new Font("隶书", Font.PLAIN, 20));
        cancelButton.setEnabled(false);
        add(cancelButton);

        statusLabel = new JLabel("等待操作...", SwingConstants.CENTER);
        statusLabel.setLocation(50, 170);
        statusLabel.setSize(300, 30);
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel);

        confirmButton.addActionListener(e -> startConnection());
        cancelButton.addActionListener(e -> stopConnection());
    }

    private void stopConnection() {
        stop();
        confirmButton.setEnabled(true);
        roomField.setEnabled(true);
        cancelButton.setEnabled(false);
        statusLabel.setText("已取消");
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
        cancelButton.setEnabled(true);
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

                // 用户名大的进行随机分配
                if (localUserName.compareTo(remoteUser) > 0) {
                    boolean localIsRed = new java.util.Random().nextBoolean();
                    if (localIsRed) {
                        sendStart(localUserName, remoteUser);
                        initGame(localUserName, remoteUser);
                    } else {
                        sendAssignRoles(remoteUser, localUserName);
                    }
                }
                
            } else if ("ASSIGN_ROLES".equals(type)) {
                if (connected) return;
                String redName = json.optString("red");
                String blackName = json.optString("black");
                
                if (localUserName.equals(redName)) {
                    peerAddress = address;
                    peerPort = port;
                    sendStart(redName, blackName);
                    initGame(redName, blackName);
                }

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

    private void sendAssignRoles(String redName, String blackName) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "ASSIGN_ROLES");
            json.put("red", redName);
            json.put("black", blackName);
            sendPacket(json.toString());
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
                
                chessBoard = new ChessBoard(model);
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
