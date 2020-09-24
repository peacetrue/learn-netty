package com.github.peacetrue.xiamen.server;

import com.github.peacetrue.xiamen.BankRequest;
import com.github.peacetrue.xiamen.BankResponse;

/**
 * @author : xiayx
 * @since : 2020-09-23 23:47
 **/
public interface BankService<T> {

    BankResponse<Object> invoke(BankRequest<T> bankRequest);

}
