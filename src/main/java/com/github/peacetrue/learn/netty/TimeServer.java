package com.github.peacetrue.learn.netty;

/**
 * @author : xiayx
 * @since : 2020-09-21 13:01
 **/
public class TimeServer extends DiscardServer {
    public TimeServer(int port) {
        super(port);
        setChannelHandler(new TimeServerHandler());
    }
}
