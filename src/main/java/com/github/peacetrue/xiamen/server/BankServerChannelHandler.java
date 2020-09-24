package com.github.peacetrue.xiamen.server;

import com.github.peacetrue.xiamen.BankRequest;
import com.github.peacetrue.xiamen.BankResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : xiayx
 * @since : 2020-09-23 12:21
 **/
@Slf4j
@Setter
@Getter
@ChannelHandler.Sharable
@SuppressWarnings("unchecked")
public class BankServerChannelHandler extends ChannelInboundHandlerAdapter {

    private BankService<Object> bankService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("read msg[{}] from client", msg);
        BankRequest bankRequest = (BankRequest) msg;
        BankResponse<?> bankResponse = bankService.invoke(bankRequest);
        ctx.writeAndFlush(bankResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
