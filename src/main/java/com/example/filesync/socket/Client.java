package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.filesync.entity.FolderInfo;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 发送端
 */
public class Client {
    /**
     * UDP广播寻址
     */
    public static <T> void broadcast(Message<T> msg) throws IOException {
        try (DatagramSocket ds = new DatagramSocket()) {
            // TODO 路由器用255.255.255.255,手机热点用网络段+255，如192.168.23.255
            ds.connect(InetAddress.getByName("192.168.23.255"), 9999); // 连接指定服务器和端口
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
            dos.writeUTF(JSON.toJSONString(folderInfo));
            dos.flush();

            receiveFile(new DataInputStream(socket.getInputStream()));  // 接收文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 接收文件
     */
    public static void receiveFile(DataInputStream dis) throws IOException {
        RemoteFolder remoteFolder = JSONObject.parseObject(dis.readUTF(), RemoteFolder.class);
        String basePath = remoteFolder.getLocalPath(); // 读取本地保存路径
        System.out.println("保存路径：" + basePath);
        // 读取文件数
        int numOfFiles = dis.readInt();
        for (int i = 0; i < numOfFiles; i++) {
            //读取文件信息
            JSONObject info = JSON.parseObject(dis.readUTF());
            String fileName = info.getString("name");
            long fileLength = info.getLong("length");

            //创建文件实例和文件输出流
            String path = basePath + fileName;
            createFileRecursion(path, 0); //递归创建文件
            File file = new File(path);

            //开始接受文件主体
            byte[] bytes = new byte[1024];
            int length = 0;
            try (FileOutputStream fos = new FileOutputStream(file)) {
                while (((length = dis.read(bytes, 0, bytes.length)) != -1)) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                    fileLength -= length;

                    if (fileLength == 0) {
                        break;
                    }
                    if (fileLength < bytes.length) {
                        bytes = new byte[(int) fileLength];
                    }
                }
            }
        }
    }

    /**
     * 递归创建文件
     */
    public static void createFileRecursion(String fileName, Integer height) throws IOException {
        System.out.println(fileName);
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            // 如果文件存在
            return;
        }
        if (Files.exists(path.getParent())) {
            // 如果父级文件存在，直接创建文件
            if (height == 0) {
                Files.createFile(path);
            } else {
                Files.createDirectory(path);
            }
        } else {
            createFileRecursion(path.getParent().toString(), height + 1);
            // 这一步能保证path的父级一定存在了，现在需要把自己也建一下
            createFileRecursion(fileName, height);
        }
    }
}
