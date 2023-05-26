package com.example.filesync.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket accept = serverSocket.accept();
                ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                threadPoolExecutor.execute(new Thread(new ServerThread(accept)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
