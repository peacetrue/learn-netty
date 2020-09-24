package com.github.peacetrue.learn.io.bio;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.locks.LockSupport;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:27
 **/
@Setter
@Getter
public class BIOClient {

    private String host;
    private int port;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public void start() throws IOException {
        System.out.println("启动服务");
        socket = new Socket();
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.connect(new InetSocketAddress(host, port));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void startBlock() throws IOException {
        this.start();
        if (thread0 != null) LockSupport.unpark(thread0);
        thread = Thread.currentThread();
        LockSupport.park();
        System.out.println("退出");
    }

    private Thread thread0;
    private Thread thread;

    public void startSync() {
        new Thread(() -> {
            try {
                this.startBlock();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
        thread0 = Thread.currentThread();
        LockSupport.park();
    }

    public void close() throws IOException {
        socket.close();
        if (thread != null) LockSupport.unpark(thread);
    }

}
