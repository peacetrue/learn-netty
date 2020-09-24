package com.github.peacetrue.learn.netty;

/**
 * @author : xiayx
 * @since : 2020-09-21 12:22
 **/
public class EchoServer extends DiscardServer {
    public EchoServer(int port) {
        super(port);
        setChannelHandler(new EchoServerHandler());
    }
}
