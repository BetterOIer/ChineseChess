package edu.sustech.xiangqi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPChatApplication {
    
    // 简单的UDP聊天应用
    static class UDPChat {
        private static final int PORT = 9999;
        private DatagramSocket socket;
        private boolean running;
        
        public void start() throws IOException {
            socket = new DatagramSocket(PORT);
            running = true;
            
            System.out.println("UDP聊天启动，端口: " + PORT);
            System.out.println("输入消息发送，输入'exit'退出");
            
            // 接收消息线程
            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();
            
            // 发送消息线程
            Thread sendThread = new Thread(this::sendMessages);
            sendThread.start();
            
            try {
                receiveThread.join();
                sendThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        private void receiveMessages() {
            byte[] buffer = new byte[1024];
            
            while (running) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    
                    String message = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("收到来自 " + packet.getAddress() + ": " + message);
                    
                    if ("exit".equalsIgnoreCase(message.trim())) {
                        running = false;
                    }
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        private void sendMessages() {
            try {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                
                while (running) {
                    System.out.print("输入消息: ");
                    String message = scanner.nextLine();
                    
                    if ("exit".equalsIgnoreCase(message.trim())) {
                        running = false;
                        break;
                    }
                    
                    byte[] data = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(
                        data, data.length, 
                        InetAddress.getByName("localhost"), PORT
                    );
                    
                    socket.send(packet);
                }
                
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void stop() {
            running = false;
            if (socket != null) {
                socket.close();
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UDPChat chat = new UDPChat();
            chat.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}