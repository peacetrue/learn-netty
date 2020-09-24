package com.github.peacetrue.learn.io.nio;

import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2020-09-22 04:18
 **/
class NIOClientTest {

    @Test
    void start() {
        NIOClient nioClient = new NIOClient();
        nioClient.setHost("localhost");
        nioClient.setPort(8888);
        nioClient.startBackground();
    }
}
