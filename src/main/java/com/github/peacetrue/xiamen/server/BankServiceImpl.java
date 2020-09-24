package com.github.peacetrue.xiamen.server;

import com.github.peacetrue.xiamen.BankRequest;
import com.github.peacetrue.xiamen.BankResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiayx
 * @since : 2020-09-23 23:53
 **/
@Data
@SuppressWarnings("unchecked")
public class BankServiceImpl implements BankService<Object> {

    private Map<String, BankService> bankServices = new HashMap<>();

    @Override
    public BankResponse<Object> invoke(BankRequest<Object> bankRequest) {
        BankRequest.BankSysHead requestSysHead = bankRequest.getSysHead();

        BankService bankService = bankServices.get(requestSysHead.getServiceCode() + requestSysHead.getServiceScene());
        BankResponse bankResponse = bankService.invoke(bankRequest);

        BankResponse.BankSysHead responseSysHead = new BankResponse.BankSysHead();
        responseSysHead.setServiceCode(requestSysHead.getServiceCode());
        responseSysHead.setServiceScene(requestSysHead.getServiceScene());
        responseSysHead.setConsumerId(requestSysHead.getConsumerId());
        bankResponse.setSysHead(responseSysHead);
        return bankResponse;
    }
}
