package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.filesync.common.ApplicationContextUtil;
import com.example.filesync.common.CommonUtil;
import com.example.filesync.entity.LocalFolder;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;
import com.example.filesync.service.RemoteFolderService;
import com.example.filesync.service.impl.RemoteFolderServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpHandle {
    private static RemoteFolderService remoteFolderService;
    static {
        //从 Spring 容器中 获取 startFlowService 对象
        remoteFolderService = ApplicationContextUtil.getBean(RemoteFolderService.class);
    }

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
        if (folder.getDeviceId().equals(CommonUtil.getMac())) {
            try (DatagramSocket ds = new DatagramSocket()) {
                ds.connect(InetAddress.getByName(message.getSrcIp()), 9999); // 回应
                String myIp = CommonUtil.getLocalHostExactAddress();

                Message<String> resp;
                // 本地是否有这个文件夹
                LocalFolder localFolder = remoteFolderService.searchLocalFolder(folder.getFolderId());
                if (localFolder != null) {
                    resp = new Message<>(Message.findRemoteFolderResponse, new File(localFolder.getFolderPath()).getName(), myIp);
                } else {
                    resp = new Message<>(Message.findRemoteFolderResponse, "", myIp);
                }

                System.out.println(resp);
                byte[] data = JSON.toJSONString(resp).getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                ds.send(packet);
                ds.disconnect();
            }
        }
    }

    public static void findRemoteFolderCallBack(Message<String> message) {
        remoteFolderService.setResp(message);
    }
}
