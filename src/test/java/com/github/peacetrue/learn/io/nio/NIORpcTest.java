package com.github.peacetrue.learn.io.nio;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2020-09-22 09:29
 **/
class NIORpcTest {

    @Test
    void invoke() throws Exception {
        NIOClient nioClient = getNioClient();

        NIORpc nioRpc = new NIORpc(nioClient);
        System.out.println(nioRpc.invoke("line"));
        nioRpc.close();
    }

    private NIOClient getNioClient() {
        NIOClient nioClient = new NIOClient();
        nioClient.setHost("localhost");
        nioClient.setPort(8888);
        nioClient.startBackground();
        return nioClient;
    }

    @Test
    void concurrentInvoke() throws Exception {
        int nThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        NIOClient nioClient = getNioClient();
        NIORpc nioRpc = new NIORpc(nioClient);
        List<Callable<Object>> callables = IntStream.range(0, nThreads)
                .mapToObj(i -> (Callable<Object>) () -> nioRpc.invoke(i + ""))
                .collect(Collectors.toList());
        List<Object> list = executorService.invokeAll(callables).stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                return e;
            }
        }).collect(Collectors.toList());
        //并发无序性
        System.out.println(list);
        nioRpc.close();
    }
}
