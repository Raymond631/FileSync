package com.example.filesync.utils;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

/**
 * 接收文件
 */
public class Server {
    private static int port = 6699;

    public static void startFileServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket accept = serverSocket.accept();
                ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                threadPoolExecutor.execute(new Thread(new FileThread(accept)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startMessageServer() throws Exception{
        System.out.println("=====ClientB接收======");
        DatagramSocket socket=new DatagramSocket(port);
        byte[] buffer=new byte[1024*64];
        DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
        socket.receive(packet);
        int len=packet.getLength();
        String rs=new String(buffer,0,len);
        //想要的ip的编号
        String want=rs.substring(0,id.length());

        System.out.println("收到了IP为："+packet.getAddress()+"\n端口为："+packet.getPort()+" 的消息");
        System.out.println("消息内容："+rs);
        if(want.equals(id)){
            //广播的端口
            String Port=rs.substring(id.length()+1,id.length()+1+4);
            int bcPort=Integer.parseInt(Port);
            //广播的ip
            String ip=rs.substring(rs.length()-12);
            InetAddress bcIP= InetAddress.getByName(ip);
            System.out.println(want);
            System.out.println(bcPort);
            System.out.println(ip);
            sendIP(bcIP,bcPort);
        }
        socket.close();
    }
    public static void sendIP(InetAddress addr,int port) throws IOException {
        System.out.println("=====发送自己的ip======");
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = InetAddress.getLocalHost().toString().getBytes();
        DatagramPacket packet = new DatagramPacket( buffer, buffer.length,addr, port);
        socket.send(packet);
        socket.close();
    }

}
