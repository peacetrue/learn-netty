package com.github.peacetrue.learn.io.selector;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : xiayx
 * @since : 2020-09-23 10:01
 **/
class SelectorNIOServerTest {

    @Test
    void start() throws Exception {
        SelectorNIOServer selectorNIOServer = new SelectorNIOServer();
        selectorNIOServer.setPort(8888);
        selectorNIOServer.start();
        // telnet localhost 8888
    }
}
