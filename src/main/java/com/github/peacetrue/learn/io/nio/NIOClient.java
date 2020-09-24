package com.github.peacetrue.learn.io.nio;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.LockSupport;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:27
 **/
@Setter
@Getter
public class NIOClient {

    private String host;
    private int port;
    private SocketChannel socket;

    public void start() throws IOException {
        System.out.println("启动服务");
        socket = SocketChannel.open();
        socket.setOption(StandardSocketOptions.TCP_NODELAY, true);
        socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        socket.connect(new InetSocketAddress(host, port));
        socket.configureBlocking(false);
    }

    private Thread thread;

    public void startBackground() {
        Thread thread = Thread.currentThread();
        new Thread(() -> {
            try {
                this.start();
                LockSupport.unpark(thread);
                //后台运行
                this.thread = Thread.currentThread();
                LockSupport.park();
                System.out.println("退出");
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    this.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
        LockSupport.park();//阻塞直到服务启动完成，否则服务未启动完成，获取到 null Socket
    }

    public void close() throws IOException {
        socket.close();
        if (thread != null) LockSupport.unpark(thread);
    }

}
