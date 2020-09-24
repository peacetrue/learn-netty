package com.github.peacetrue.learn.io.bio;

import com.github.peacetrue.learn.io.bio.BIOClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:34
 **/
class BIOClientTest {

    @Test
    void start() throws IOException {
        BIOClient bioClient = new BIOClient();
        bioClient.setHost("localhost");
        bioClient.setPort(8888);
        bioClient.startSync();
    }

}
