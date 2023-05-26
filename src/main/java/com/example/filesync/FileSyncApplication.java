package com.example.filesync;

import com.example.filesync.utils.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSyncApplication.class, args);
        Server server = new Server(6699);
        server.start();
    }

}
