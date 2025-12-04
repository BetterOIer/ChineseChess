package edu.sustech.xiangqi.ui;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import org.json.JSONObject;

import edu.sustech.xiangqi.model.*;

public class Connection extends JFrame{
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
    private int active = 1;
    private String candidate = null;
    
    public Connection(){
        setTitle("等待连接");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,200);
        setLocationRelativeTo(null);
        
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
                    e2.printStackTrace();
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
        // 启用广播并发送到广播地址（向整个局域网/全网广播）
        try {
            socket.setBroadcast(true);
        } catch (SocketException se) {
            se.printStackTrace();
        }
        peerAddress = InetAddress.getByName("255.255.255.255");
        /* PORT = PORT; */

        // 接收消息线程
        Thread receiveThread = new Thread(this::receiveMessages);
        receiveThread.start();
        
        // 发送消息线程（周期性发送 Handshake，连接后可以发送更新）
        Thread sendThread = new Thread(this::sendMessages);
        sendThread.start();

        while(true){
            if(chessBoardModel!=null && ((chessBoardModel.getType()&8)!=0)){
                chessBoard.dispose();
                dispose();
                stop();
            }
        }
    }

    private void sendMessages(){
        while(running){
            System.out.println(""+connected+" "+confirm);
            try{
                try{
                    if((!connected)&&(!confirm)){
                        String handshake = encodeBuff("Handshake",DBOperationUser.getUserInUse(),room.getText());
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
                        String confirm = encodeBuff("Confirm",DBOperationUser.getUserInUse(),room.getText());
                        byte[] data = confirm.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
                        try{
                            socket.send(packet);
                            System.out.println("sent:"+confirm);
                        }catch(IOException ignore){
                            ignore.printStackTrace();
                        }
                        Thread.sleep(1000); // 间隔发送，避免忙循环
                    }else if(connected && active>0){
                        String board = encodeBuff("Board", DBOperationUser.getUserInUse(), chessBoardModel.toString());
                        byte[] data = board.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
                        try{
                            socket.send(packet);
                            System.out.println("sent:"+board);
                        }catch(IOException ignore){
                            ignore.printStackTrace();
                        }
                        active--;
                        Thread.sleep(33); // 间隔发送，避免忙循环
                    }else if(connected){
                        Thread.sleep(10000);
                        /* if (chessBoard != null) {
                            Thread listenerThread = new Thread(() -> {
                                chessBoard.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mousePressed(MouseEvent e) {
                                        try {
                                            String mouseMsg = encodeBuff("Mouse", user, e.getX() + "," + e.getY());
                                            byte[] data = mouseMsg.getBytes();
                                            DatagramPacket packet = new DatagramPacket(data, data.length, peerAddress, PORT);
                                            socket.send(packet);
                                            System.out.println("sent:" + mouseMsg);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                            });
                            listenerThread.start();
                            break;
                        } */
                    }
                }catch(SQLException e){
                    e.printStackTrace();
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
                e.printStackTrace();
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
        try{
            if(userStr.equals(DBOperationUser.getUserInUse().getName())) return;
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(aim.equals("Handshake")){
            if((!this.connected) && (!this.confirm)){
                if(message.equals(room.getText())){
                    this.confirm=true;
                    this.candidate=userStr;
                }
            }
        }
        if(aim.equals("Confirm")){
            if((!this.connected) && (this.confirm) && (this.active>0)){
                if(this.candidate.equals(userStr)){
                    try{
                        if(DBOperationUser.getUserByName(userStr)==null){
                            DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(),userStr,null, 2));
                        }
                        chessBoardModel = new ChessBoardModel(DBOperationBoard.getBoardCount(), 2, DBOperationUser.getUserInUse(), DBOperationUser.getUserByName(userStr),DBOperationUser.getUserInUse(), true);
                        DBOperationBoard.insertBoard(chessBoardModel);
                        chessBoard = new ChessBoard(chessBoardModel);
                        this.connected=true;
                        chessBoard.setVisible(true);
                        setVisible(false);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            }else if((!this.connected) && (!this.confirm)){
                this.confirm=true;
                this.candidate=userStr;
                this.active=0;
            }
        }
        if(aim.equals("Board")){
            try{
                chessBoardModel = convert2Board(message);
                chessBoard = new ChessBoard(chessBoardModel);
                DBOperationBoard.insertBoard(chessBoardModel);
                this.connected=true;
                chessBoard.setVisible(true);
                setVisible(false);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(aim.equals("Mouse")){
            String coord = message.trim();
            if (coord.startsWith("(") && coord.endsWith(")")) {
                coord = coord.substring(1, coord.length() - 1);
            }
            String[] parts = coord.split(",");
            if (parts.length >= 2) {
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                chessBoard.getPanel().handleMouseClick(x,y);
            }
        }
    }

    private ChessBoardModel convert2Board(String msg)throws SQLException{
        {
            if (msg == null || msg.trim().isEmpty()) return null;

            String[] parts = msg.split(" ");
            if (parts.length < 6) return null;

            String name = parts[0];
            int boardType = Integer.parseInt(parts[1]);

            int len = parts.length;
            boolean whoseTurn = Boolean.parseBoolean(parts[len - 1]);
            String userBlack = parts[len - 2];
            String userRed = parts[len - 3];

            String description;
            if (len > 6) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i <= len - 4; i++) {
                    if (sb.length() > 0) sb.append(" ");
                    sb.append(parts[i]);
                }
                description = sb.toString();
            } else {
                description = parts[2];
            }

            User redUser = null;
            User blackUser = null;
            try {
                redUser = DBOperationUser.getUserByName(userRed);
                if (redUser == null) {
                    DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(), userRed, null, 2));
                    redUser = DBOperationUser.getUserByName(userRed);
                }
                blackUser = DBOperationUser.getUserByName(userBlack);
                if (blackUser == null) {
                    DBOperationUser.insertUser(new User(DBOperationUser.getUserCount(), userBlack, null, 2));
                    blackUser = DBOperationUser.getUserByName(userBlack);
                }
                ChessBoardModel model = new ChessBoardModel(DBOperationBoard.getBoardCount(), name, boardType, description, redUser, blackUser, DBOperationUser.getUserInUse(), whoseTurn);
                return model;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
        
    public void stop() {
        running = false;
        connected = false;
        confirm = false;
        active = 1;
        if (socket != null) {
            socket.close();
        }
    }
}
