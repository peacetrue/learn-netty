package com.github.peacetrue.xiamen.client;

import com.github.peacetrue.xiamen.BankResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author : xiayx
 * @since : 2020-09-23 12:21
 **/
@Slf4j
@Getter
public class BankClientChannelHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, BiConsumer<BankResponse<?>, Throwable>> callbacks = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("received msg(BankResponse) and trigger callback");
        BankResponse<?> bankResponse = (BankResponse<?>) msg;
        log.debug("msg(BankResponse): {}", bankResponse);
        BiConsumer<BankResponse<?>, Throwable> callback = callbacks.remove(bankResponse.getSysHead().getConsumerId());
        if (callback == null) {
            log.warn("the callback of msg[{}] is missing", bankResponse.getSysHead().getConsumerId());
        } else {
            callback.accept(bankResponse, null);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("receive msg error", cause);
        ctx.close();
    }
}
