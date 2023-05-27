package com.example.filesync.socket;

import com.alibaba.fastjson2.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpServer {

    public static void startMessageServer() throws Exception {
        // 监听指定端口
        try (DatagramSocket ds = new DatagramSocket(9999)) {
            while (true) { // 无限循环
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet); // 收取一个UDP数据包
                // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
                // 将其按UTF-8编码转换为String:
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                int type = JSONObject.parseObject(msg).getIntValue("type");
                // 消息处理和回复
                UdpHandle.process(msg, type);
            }
        }
    }
}
