package com.github.peacetrue.xiamen;

/**
 * @author : xiayx
 * @since : 2020-09-23 23:37
 **/
public interface BankRequestResolver {

    /** 解析请求体 */
    Object resolveBody(BankRequest<?> bankRequest) throws Exception;

}
