package com.github.peacetrue.learn.netty;

import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2020-09-21 13:02
 **/
class TimeServerTest {

    @Test
    void start() throws Exception {
        TimeServer timeServer = new TimeServer(7779);
        timeServer.start();
        // rdate -o 7779
        // mac 不支持 -o 选项
        // telnet localhost 7779
        // telnet 不能正确回显时间
    }

}
