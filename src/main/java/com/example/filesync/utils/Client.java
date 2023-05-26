package com.example.filesync.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {
    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(ip, port); OutputStream outputStream = socket.getOutputStream(); DataOutputStream dos = new DataOutputStream(outputStream)) {
            String[] fileNames = {"D:/Document/文件分布式同步服务.docx", "D:/Document/《软件开发架构平台》实验指导书2021级_实验二.doc"};
            dos.writeInt(fileNames.length);
            dos.flush();

            for (int i = 0; i < fileNames.length; i++) {
                File file = new File(fileNames[i]);

                //写入文件信息
                JSONObject info = new JSONObject();
                info.put("name", file.getName());
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
            e.printStackTrace();
        }
    }
}
