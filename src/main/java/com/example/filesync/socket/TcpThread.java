package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.filesync.common.ApplicationContextUtil;
import com.example.filesync.common.CommonUtil;
import com.example.filesync.entity.FolderInfo;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class TcpThread implements Runnable {
    private static RemoteFolderService remoteFolderService;

    static {
        //从 Spring 容器中 获取 startFlowService 对象
        remoteFolderService = ApplicationContextUtil.getBean(RemoteFolderService.class);
    }

    private final Socket client;

    public TcpThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStream inputStream = client.getInputStream(); DataInputStream dis = new DataInputStream(inputStream)) {
            responseSync(dis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 给别人发文件
     */
    public void responseSync(DataInputStream dis) throws IOException {
        FolderInfo folderInfo = JSON.parseObject(dis.readUTF(), FolderInfo.class);
        Map<String, LocalDateTime> remoteInfo = folderInfo.getInfo();
        System.out.println(remoteInfo);

        RemoteFolder remoteFolder = folderInfo.getFolder();
        String localPath = remoteFolderService.searchLocalFolder(remoteFolder.getFolderId()).getFolderPath();
        Map<String, LocalDateTime> localInfo = CommonUtil.scanDirectory(localPath);
        System.out.println(localInfo);

        Map<String, LocalDateTime> fileInfo = new HashMap<>();
        for (Map.Entry<String, LocalDateTime> entry : localInfo.entrySet()) {
            if (remoteInfo.get(entry.getKey()) == null) {  // 新文件
                fileInfo.put(localPath + entry.getKey(), entry.getValue());
            } else if (remoteInfo.get(entry.getKey()).isBefore(entry.getValue())) {  // 修改过的文件
                fileInfo.put(localPath + entry.getKey(), entry.getValue());
            }
        }
        System.out.println(fileInfo);

        try (OutputStream outputStream = client.getOutputStream(); DataOutputStream dos = new DataOutputStream(outputStream)) {
            dos.writeUTF(JSON.toJSONString(remoteFolder));  // 传回foldId和localPath，方便接收端保存
            dos.flush();

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
                int length;
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
