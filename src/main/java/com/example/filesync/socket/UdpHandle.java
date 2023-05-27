package com.example.filesync.socket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.filesync.common.CommonUtils;
import com.example.filesync.entity.Message;
import com.example.filesync.entity.RemoteFolder;

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
        if (message.getData().getDeviceId().equals(CommonUtils.getMac())) {
            try (DatagramSocket ds = new DatagramSocket()) {
                ds.connect(InetAddress.getByName(message.getSrcIp()), 9999); // 回应
                Message<String> resp = new Message<>(Message.findRemoteFolderResponse, InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString());
                byte[] data = JSON.toJSONString(resp).getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length);
                ds.send(packet);
                ds.disconnect();
            }
        }
    }

    public static void findRemoteFolderCallBack(Message<String> message) throws IOException {
        System.out.println(message.getData());
    }
}
