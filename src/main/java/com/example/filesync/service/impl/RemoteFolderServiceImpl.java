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
    public Message<Boolean> resp = null;  //回调“信箱”
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
    public Boolean addFolder(RemoteFolder folder) throws IOException {
        try (DatagramSocket ds = new DatagramSocket()) {
            ds.connect(InetAddress.getByName("255.255.255.255"), 9999); // 连接指定服务器和端口
            Message<RemoteFolder> msg = new Message<>(Message.findRemoteFolder, folder, CommonUtils.getLocalHostExactAddress());
            byte[] data = JSON.toJSONString(msg).getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
            ds.disconnect();

            // 检查“信箱”是否有回复
            long t1 = System.currentTimeMillis();
            while (resp == null) {
                long t2 = System.currentTimeMillis();
                if (t2 - t1 > 10000) {
                    return false;  // 超时
                }
                Thread.sleep(1000);  // 1秒检查一次”信箱“
            }
            Boolean hasFolder = resp.getData();
            resp = null;  // 重置“信箱”

            if (hasFolder) { // 有这个文件夹
                int i = remoteFolderMapper.insertFolder(folder);
                System.out.println("影响行数：" + i);
                return true;
            } else { // 没有这个文件夹
                return false;
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
