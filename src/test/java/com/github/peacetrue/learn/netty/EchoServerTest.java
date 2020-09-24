package com.github.peacetrue.learn.netty;

import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2020-09-21 12:25
 **/
class EchoServerTest {

    @Test
    void start() throws Exception {
        EchoServer echoServer = new EchoServer(7778);
        echoServer.start();
    }
}
