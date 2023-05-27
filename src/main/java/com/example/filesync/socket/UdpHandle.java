package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;
import com.example.filesync.service.impl.RemoteFolderServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpHandle {
    public static void process(String msg, int type) throws IOException {
        switch (type) {
            // 寻址回复
            case Message.findRemoteFolder -> findRemoteFolderResponse(JSON.parseObject(msg, new TypeReference<>() {
            }));
            // 收到寻址回复
            case Message.findRemoteFolderResponse -> findRemoteFolderCallBack(JSON.parseObject(msg, new TypeReference<>() {
            }));
        }
    }

    public static void findRemoteFolderResponse(Message<RemoteFolder> message) throws IOException {
        // 如果发送方找的是本机
        RemoteFolder folder = message.getData();
        if (folder.getDeviceId().equals(CommonUtils.getMac())) {
            try (DatagramSocket ds = new DatagramSocket()) {
                ds.connect(InetAddress.getByName(message.getSrcIp()), 9999); // 回应
                String myIp = CommonUtils.getLocalHostExactAddress();

                Message<String> resp;
                // 本地是否有这个文件夹
                RemoteFolderService service = new RemoteFolderServiceImpl();
                LocalFolder localFolder = service.searchLocalFolder(folder.getFolderId());
                if (localFolder != null) {
                    resp = new Message<>(Message.findRemoteFolderResponse, new File(localFolder.getFolderPath()).getName(), myIp);
                } else {
                    resp = new Message<>(Message.findRemoteFolderResponse, "", myIp);
                }

                byte[] data = JSON.toJSONString(resp).getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                ds.send(packet);
                ds.disconnect();
            }
        }
    }

    public static void findRemoteFolderCallBack(Message<String> message) {
        RemoteFolderServiceImpl.resp=message;
    }
}
