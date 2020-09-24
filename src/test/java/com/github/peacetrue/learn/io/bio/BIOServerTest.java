package com.github.peacetrue.learn.io.bio;

import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:17
 **/
class BIOServerTest {

    @Test
    void start() throws Exception {
        BIOServer bioServer = new BIOServer();
        bioServer.setPort(8888);
        bioServer.start();
        //telnet localhost 8888
    }
}
