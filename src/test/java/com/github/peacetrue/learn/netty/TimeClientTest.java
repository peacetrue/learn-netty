package com.github.peacetrue.learn.netty;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2020-09-21 13:24
 **/
class TimeClientTest {

    @Test
    void start() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.invokeAll(IntStream.range(0, 100).mapToObj(i -> (Callable<Object>) () -> {
            try {
                TimeClient.start("localhost", 7779);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }
}
