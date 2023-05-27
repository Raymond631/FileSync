package com.example.filesync.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:45
 * @description
 */
@Service
public class RemoteFolderServiceImpl implements RemoteFolderService {
    @Override
    public String addFolder(RemoteFolder folder) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(1000);
        ds.connect(InetAddress.getByName("255.255.255.255"), 9999); // 连接指定服务器和端口
        // 发送:
        Message<RemoteFolder> msg = new Message<>(Message.findRemoteFolder, folder);
        byte[] data = JSON.toJSONString(msg).getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        ds.send(packet);
        // 接收:
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
        ds.disconnect();
        return resp;
    }
}
