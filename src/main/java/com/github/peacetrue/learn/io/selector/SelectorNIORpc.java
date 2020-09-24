package com.github.peacetrue.learn.io.selector;

import com.github.peacetrue.learn.io.nio.NIOServer;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:27
 **/
@Setter
public class SelectorNIORpc {

    private SelectorNIOClient bioClient;

    public SelectorNIORpc(SelectorNIOClient bioClient) {
        this();
        this.bioClient = bioClient;
    }

    private BlockingQueue<String> lines = new ArrayBlockingQueue<>(10);

    Thread thread;
    volatile boolean run = true;

    public SelectorNIORpc() {
        thread = new Thread(() -> {
            try {
                System.out.println("read thead enabled");

                ByteBuffer byteBuffer = ByteBuffer.allocate(8);
                Selector selector = bioClient.getSelector();
                while (run) {
                    if (selector.select(10) == 0) continue;
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey next = iterator.next();
                        //next.channel()
                    }
                    Set<SelectionKey> selectionKeys = (Set<SelectionKey>) iterator;
                    String line = NIOServer.readLine(bioClient.getSocket(), byteBuffer);
                    System.out.println("line:" + line);
                    lines.put(line);
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
