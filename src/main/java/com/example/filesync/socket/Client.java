package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.filesync.entity.Host;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 发送端
 */
public class Client {
    // private static String ip = "localhost";
    // private static int port = 6699;

    public static void sendFileInfo(Map<String, LocalDateTime> fileInfo, Host host) {
        try (Socket socket = new Socket(host.getIp(), host.getPort()); DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            dos.writeUTF(JSON.toJSONString(fileInfo));
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void sendFile(Map<String, LocalDateTime> fileInfo, Host host) {
        try (Socket socket = new Socket(host.getIp(), host.getPort()); OutputStream outputStream = socket.getOutputStream(); DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeInt(fileInfo.size());
            dos.flush();

            for (Map.Entry<String, LocalDateTime> entry : fileInfo.entrySet()) {
                File file = new File(entry.getKey());

                //写入文件信息
                JSONObject info = new JSONObject();
                info.put("name", entry.getValue());
                info.put("length", file.length());
                dos.writeUTF(JSON.toJSONString(info));
                dos.flush();

                //写入文件内容
                byte[] bytes = new byte[1024];
                int length = 0;
                try (FileInputStream fis = new FileInputStream(file);) {
                    while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                        outputStream.write(bytes, 0, length);
                        outputStream.flush();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
