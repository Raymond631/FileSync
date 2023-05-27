package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpServer {

    public static void startMessageServer() throws Exception {
        DatagramSocket ds = new DatagramSocket(9999); // 监听指定端口
        while (true) { // 无限循环
            // 数据缓冲区:
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet); // 收取一个UDP数据包
            // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
            // 将其按UTF-8编码转换为String:
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            int type = JSONObject.parseObject(s).getIntValue("type");
            // 消息处理和回复
            process(type, s, packet, ds);
        }
    }

    public static void process(int type, String msg, DatagramPacket packet, DatagramSocket ds) throws IOException {
        switch (type) {
            // 寻址
            case Message.findRemoteFolder -> {
                Message<RemoteFolder> message = JSON.parseObject(msg, new TypeReference<>() {
                });
                if (message.getData().getDeviceId().equals(CommonUtils.getMac())) {
                    // 如果发送方找的是本机
                    byte[] data = InetAddress.getLocalHost().toString().getBytes();
                    packet.setData(data);
                    ds.send(packet);
                }
            }
        }
    }

//    public static void sendIP(InetAddress addr, int port) throws IOException {
//        System.out.println("=====发送自己的ip======");
//        DatagramSocket socket = new DatagramSocket();
//        byte[] buffer = InetAddress.getLocalHost().toString().getBytes();
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, addr, port);
//        socket.send(packet);
//        socket.close();
//    }
}
