package com.github.peacetrue.xiamen;

/**
 * @author : xiayx
 * @since : 2020-09-23 23:37
 **/
public interface BankResponseResolver {

    /** 解析响应体 */
    Object resolveBody(BankResponse<?> bankResponse) throws Exception;

}
