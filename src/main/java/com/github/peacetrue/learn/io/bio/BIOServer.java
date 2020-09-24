package com.github.peacetrue.learn.io.bio;

import lombok.Setter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 回声协议
 *
 * @author : xiayx
 * @since : 2020-09-21 22:04
 **/
@Setter
public class BIOServer {

    private int port;
    private ServerSocket serverSocket;

    public void start() throws IOException {
        System.out.printf("%s:启动服务", "1").println();
        if (serverSocket != null) return;
        this.serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.printf("接收到客户端:[%s]", socket).println();
            new Thread(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.printf("读取内容:[%s]", line).println();
                        bufferedWriter.write(line + "\n");
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

}
