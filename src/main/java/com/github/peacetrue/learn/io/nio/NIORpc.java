package com.github.peacetrue.learn.io.nio;

import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:27
 **/
@Setter
public class NIORpc {

    private NIOClient bioClient;

    public NIORpc(NIOClient bioClient) {
        this();
        this.bioClient = bioClient;
    }

    private BlockingQueue<String> lines = new ArrayBlockingQueue<>(10);

    Thread thread;
    volatile boolean run = true;

    public NIORpc() {
        thread = new Thread(() -> {
            try {
                System.out.println("read thead enabled");
                ByteBuffer byteBuffer = ByteBuffer.allocate(8);
                while (run) {
                    String line = NIOServer.readLine(bioClient.getSocket(), byteBuffer);
                    if (line != null) {
                        System.out.println("line:" + line);
                        lines.put(line);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void close() throws IOException {
        run = false;
        bioClient.close();
    }

    public String invoke(String line) throws IOException {
        //data:   30
        //buffer: 10
        //time:   3
        // 3个数据包么？
        // 有序
        // 服务端收到的数据包，有序么？无序
        // 简化问题：数据包的大小
        SocketChannel socketChannel = bioClient.getSocket();
        socketChannel.write(ByteBuffer.wrap((line + "\n").getBytes()));
        System.out.println("write:" + line);
        try {
            return lines.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
