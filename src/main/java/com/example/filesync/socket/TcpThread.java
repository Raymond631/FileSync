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
    private String basePath = "D:/Download";

    public TcpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStream inputStream = client.getInputStream(); DataInputStream dis = new DataInputStream(inputStream)) {
            int type = dis.readInt();
            switch (type) {
                case Client.infoTcp -> {
                    responseSync(dis);
                }
                case Client.fileTcp -> {
                    receiveFile(dis);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 给别人发文件
     */
    public void responseSync(DataInputStream dis) throws IOException {
//        FolderInfo folderInfo = JSON.parseObject(dis.readUTF(), FolderInfo.class);
//        RemoteFolder folder = folderInfo.getFolder();
//        Map<String, LocalDateTime> remoteInfo = folderInfo.getInfo();
//
//        try (OutputStream outputStream = client.getOutputStream(); DataOutputStream dos = new DataOutputStream(outputStream)) {
//            dos.writeInt(Client.fileTcp);
//            dos.flush();
//
//            dos.writeInt(fileInfo.size());
//            dos.flush();
//
//            for (Map.Entry<String, LocalDateTime> entry : fileInfo.entrySet()) {
//                File file = new File(entry.getKey());
//
//                //写入文件信息
//                JSONObject info = new JSONObject();
//                info.put("name", entry.getValue());
//                info.put("length", file.length());
//                dos.writeUTF(JSON.toJSONString(info));
//                dos.flush();
//
//                //写入文件内容
//                byte[] bytes = new byte[1024];
//                int length;
//                try (FileInputStream fis = new FileInputStream(file);) {
//                    while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
//                        outputStream.write(bytes, 0, length);
//                        outputStream.flush();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * 接收文件
     */
    public void receiveFile(DataInputStream dis) throws IOException {
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
    }

    /**
     * 递归创建文件
     */
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
