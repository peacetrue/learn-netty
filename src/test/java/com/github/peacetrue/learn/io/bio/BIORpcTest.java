package com.github.peacetrue.learn.io.bio;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : xiayx
 * @since : 2020-09-21 22:55
 **/
class BIORpcTest {

    @Test
    void invoke() throws IOException {
        BIORpc bioRpc = new BIORpc(getBioClient());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(bioRpc.invoke(line));
        }
    }

    private BIOClient getBioClient() {
        BIOClient bioClient = new BIOClient();
        bioClient.setHost("localhost");
        bioClient.setPort(8888);
        bioClient.startSync();
        return bioClient;
    }

    @Test
    void close() throws IOException {
        BIOClient bioClient = getBioClient();
        BIORpc bioRpc = new BIORpc(bioClient);
        System.out.println(bioRpc.invoke("hi"));
        bioClient.close();
    }

    @Test
    void concurrentInvoke() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        BIOClient bioClient = getBioClient();
        BIORpc bioRpc = new BIORpc(bioClient);
        List<Callable<Object>> callables = IntStream.range(0, 20)
                .mapToObj(i -> (Callable<Object>) () -> bioRpc.invoke(i + ""))
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
    }

    public static void main(String[] args) throws IOException {
        new BIORpcTest().invoke();
    }
}
