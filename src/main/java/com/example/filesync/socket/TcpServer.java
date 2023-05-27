package com.example.filesync.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 接收文件
 */
public class TcpServer {
    private static int port = 6666;

    public static void startFileServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket accept = serverSocket.accept();
                ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                threadPoolExecutor.execute(new Thread(new TcpThread(accept)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
