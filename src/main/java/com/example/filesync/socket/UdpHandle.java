package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
public class UdpHandle {
    private static UdpHandle udpHandle;
    @Autowired
    private RemoteFolderService remoteFolderService;

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

                Message<Boolean> resp;
                // 本地是否有这个文件夹
                if (udpHandle.remoteFolderService.searchLocalFolder(folder.getFolderId())) {
                    resp = new Message<>(Message.findRemoteFolderResponse, true, myIp);
                } else {
                    resp = new Message<>(Message.findRemoteFolderResponse, false, myIp);
                }

                byte[] data = JSON.toJSONString(resp).getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                ds.send(packet);
                ds.disconnect();
            }
        }
    }

    public static void findRemoteFolderCallBack(Message<Boolean> message) {
        System.out.println(message.getData());
    }

    /**
     * 使静态类支持IoC
     */
    @PostConstruct
    public void init() {
        udpHandle = this;
        udpHandle.remoteFolderService = this.remoteFolderService;
    }
}
