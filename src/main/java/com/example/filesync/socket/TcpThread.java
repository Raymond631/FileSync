package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TcpThread implements Runnable {
    private final Socket client;
    private final String basePath = "D:/Download/ShareFolder";

    public TcpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStream inputStream = client.getInputStream(); DataInputStream dis = new DataInputStream(inputStream)) {
            //读取文件数
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFileRecursion(String fileName, Integer height) throws IOException {
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
