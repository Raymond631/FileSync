package com.example.filesync;

import com.example.filesync.socket.UdpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileSyncApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileSyncApplication.class, args);

        UdpServer.startMessageServer();
    }
}
