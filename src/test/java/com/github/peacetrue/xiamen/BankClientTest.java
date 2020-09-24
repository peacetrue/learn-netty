package com.github.peacetrue.xiamen;

import com.github.peacetrue.xiamen.client.BankClient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author : xiayx
 * @since : 2020-09-23 13:29
 **/
class BankClientTest {

    @Test
    void start() {
        BankClient bankClient = new BankClient();
        bankClient.start();
    }

    @Test
    void invoke() {
        BankClient bankClient = getBankClient();

        BankResponse<OrderVO> bankResponse = bankClient.invoke(BankRequestTest.getOrderGetBankRequest());
        System.out.println(bankResponse);
        bankClient.close();
    }

    private BankClient getBankClient() {
        BankClient bankClient = new BankClient();
        bankClient.start();
        config(bankClient);
        return bankClient;
    }

    private void config(BankClient bankClient) {
        BankRequestResolverImpl bankRequestResolver = BankRequestTest.getBankRequestResolver();
        bankClient.getBankRequestCodec().setBankRequestResolver(bankRequestResolver);

        BankResponseResolverImpl bankResponseResolver = BankRequestTest.getBankResponseResolver();
        bankClient.getBankResponseCodec().setBankResponseResolver(bankResponseResolver);
    }


    @Test
    void concurrentInvoke() throws Exception {
        BankClient bankClient = getBankClient();

        int nThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        List<Callable<Object>> callables = LongStream.range(0, nThreads)
                .mapToObj(i -> (Callable<Object>) () -> bankClient.invoke(BankRequestTest.getOrderGetBankRequest(i)))
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
        bankClient.close();
    }

    @Test
    void close() {
    }
}
