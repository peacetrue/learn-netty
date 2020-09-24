package com.github.peacetrue.xiamen;

import com.github.peacetrue.xiamen.server.BankServer;
import com.github.peacetrue.xiamen.server.BankService;
import com.github.peacetrue.xiamen.server.BankServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author : xiayx
 * @since : 2020-09-23 13:29
 **/
class BankServerTest {

    @Test
    void start() {
        BankServer bankServer = new BankServer();

        bankServer.getBankRequestCodec().setBankRequestResolver(BankRequestTest.getBankRequestResolver());
        bankServer.getBankResponseCodec().setBankResponseResolver(BankRequestTest.getBankResponseResolver());
        BankServiceImpl bankService = new BankServiceImpl();
        bankService.getBankServices().put("orderget", (BankService<OrderGet>) bankRequest -> {
            BankResponse<Object> response = new BankResponse<>();
            OrderVO orderVO = new OrderVO();
            orderVO.setId(bankRequest.getBody().getId());
            orderVO.setName(UUID.randomUUID().toString());
            response.setBody(orderVO);
            return response;
        });
        bankServer.getBankServerChannelHandler().setBankService(bankService);

        bankServer.start();
    }

    @Test
    void close() {
    }
}
