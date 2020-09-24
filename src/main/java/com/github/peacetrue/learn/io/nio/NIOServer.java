package com.github.peacetrue.learn.io.nio;

import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : xiayx
 * @since : 2020-09-22 03:32
 **/
@Setter
public class NIOServer {

    private int port;
    private Map<SocketChannel, ByteBuffer> queue = new HashMap<>();

    public void start() throws Exception {
        System.out.println("启动服务");

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        //死循环，消耗 CPU 资源，利用率 300%，心好累
        while (true) {
//            Thread.sleep(100L);
            //System.out.printf("循环：%s", ++i).println();
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.printf("接收连接：%s", socketChannel).println();
                socketChannel.configureBlocking(false);
                //TODO 连接关闭，如何释放
                queue.put(socketChannel, ByteBuffer.allocateDirect(8));
            }
            System.out.println("queue:" + queue.size());
            Iterator<Map.Entry<SocketChannel, ByteBuffer>> iterator = queue.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SocketChannel, ByteBuffer> next = iterator.next();
                SocketChannel item = next.getKey();
                if (!item.isOpen() || !item.isConnected()) {
                    System.out.println("---removed");
                    iterator.remove();
                }
                try {
                    //收到数据包的顺序可以保证么？
                    //
                    String line = readLine(item, next.getValue());
                    //缓冲区太小，一次读不完，不会继续读，按行分界符读
                    if (line != null) {
                        System.out.printf("读取行：%s", line).println();
                        item.write(ByteBuffer.wrap((line + "\n").getBytes()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("---removed");
                    iterator.remove();
                    item.close();
                }
            }
        }
    }

    public static String readLine(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        return readLineInner(socketChannel, byteBuffer);
    }

    public static String readLineInner(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        socketChannel.read(byteBuffer);
        if (byteBuffer.position() == 0) return null;
        byteBuffer.flip();
        StringBuilder builder = new StringBuilder();
        if (readToEnd(byteBuffer, builder)) return builder.toString();
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            if (readToEnd(byteBuffer, builder)) return builder.toString();
        }
        return null;
    }

    public static boolean readToEnd(ByteBuffer byteBuffer, StringBuilder builder) {
        for (int i = byteBuffer.position(); i < byteBuffer.limit(); i++) {
            char aChar = (char) byteBuffer.get();
            if (aChar == '\n') {
                byteBuffer.compact();
                return true;
            }
            builder.append(aChar);
        }
        byteBuffer.clear();
        return false;
    }
}
