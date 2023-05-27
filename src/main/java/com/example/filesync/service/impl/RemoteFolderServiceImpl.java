package com.example.filesync.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.mapper.RemoteFolderMapper;
import com.example.filesync.service.RemoteFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:45
 * @description
 */
@Service
public class RemoteFolderServiceImpl implements RemoteFolderService {
    public Message<Boolean> resp = null;
    @Autowired
    private RemoteFolderMapper remoteFolderMapper;

    /**
     * 钩子，用于回调
     */
    @Override
    public void setResp(Message<Boolean> resp) {
        this.resp = resp;
    }

    @Override
    public void addFolder(RemoteFolder folder) throws IOException {
        try (DatagramSocket ds = new DatagramSocket()) {
            ds.connect(InetAddress.getByName("255.255.255.255"), 9999); // 连接指定服务器和端口
            Message<RemoteFolder> msg = new Message<>(Message.findRemoteFolder, folder, CommonUtils.getLocalHostExactAddress());
            byte[] data = JSON.toJSONString(msg).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
            ds.disconnect();

            while (resp == null) {
                Thread.sleep(1000);
            }

            if (resp.getData()) {
                System.out.println("有这个文件夹");
            } else {
                System.out.println("没有这个文件夹");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RemoteFolder> getFolders() {
        return remoteFolderMapper.selectFolders();
    }

    @Override
    public void removeFolder(String folderId) {
        remoteFolderMapper.deleteFolder(folderId);
    }

    @Override
    public boolean searchLocalFolder(String folderId) {
        LocalFolder folder = remoteFolderMapper.selectLocalFolderById(folderId);
        return folder != null;
    }
}
