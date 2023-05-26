package com.example.filesync.service.impl;

import com.example.filesync.service.DeviceService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

/**
 * @author Raymond Li
 * @create 2023-05-26 19:45
 * @description
 */
@Service
public class DeviceServiceImpl implements DeviceService {
    private int port = 9999;
    @Override
    public void addFolder(String deviceId, String folderId) throws IOException {
        DatagramSocket socket=new DatagramSocket();
        String s = deviceId+"|"+ folderId+"|"+InetAddress.getLocalHost().toString();
        byte[] buffer=s.getBytes();

        DatagramPacket packet=new DatagramPacket(buffer,buffer.length, InetAddress.getByName("255.255.255.255"),port);
        socket.send(packet);
        socket.close();

        
    }
}
