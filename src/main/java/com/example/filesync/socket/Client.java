package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.example.filesync.entity.FolderInfo;
import com.example.filesync.entity.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 发送端
 */
public class Client {
    public static final int infoTcp = 1;
    public static final int fileTcp = 2;

    /**
     * UDP广播寻址
     */
    public static <T> void broadcast(Message<T> msg) throws IOException {
        try (DatagramSocket ds = new DatagramSocket()) {
            ds.connect(InetAddress.getByName("255.255.255.255"), 9999); // 连接指定服务器和端口
            byte[] data = JSON.toJSONString(msg).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
            ds.disconnect();
        }
    }

    /**
     * TCP单播同步请求
     */
    public static void sendFileInfo(FolderInfo folderInfo, String destIp) {
        try (Socket socket = new Socket(destIp, 6666); DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            dos.writeInt(infoTcp);
            dos.flush();

            dos.writeUTF(JSON.toJSONString(folderInfo));
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
