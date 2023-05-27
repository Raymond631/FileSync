package com.example.filesync;

import com.example.filesync.socket.TcpServer;
import com.example.filesync.socket.UdpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileSyncApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileSyncApplication.class, args);
        new Thread(new UdpServer()).start();
        System.out.println("udp监听启动成功");
        TcpServer.startFileServer();
    }
}
