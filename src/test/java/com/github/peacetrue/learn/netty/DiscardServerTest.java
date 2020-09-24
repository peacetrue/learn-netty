package com.github.peacetrue.learn.netty;

import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2020-09-21 11:33
 **/
class DiscardServerTest {

    @Test
    void start() throws Exception {
        DiscardServer discardServer = new DiscardServer(7777);
        discardServer.start();
    }
}
