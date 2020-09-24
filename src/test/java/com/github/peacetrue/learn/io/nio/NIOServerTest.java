package com.github.peacetrue.learn.io.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author : xiayx
 * @since : 2020-09-22 03:58
 **/
class NIOServerTest {

    @Test
    void start() throws Exception {
        NIOServer nioServer = new NIOServer();
        nioServer.setPort(8888);
        nioServer.start();
        // telnet localhost 8888
    }
}
