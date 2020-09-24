package com.github.peacetrue.learn.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author : xiayx
 * @since : 2020-09-21 12:24
 **/
public class EchoServerHandler extends DiscardServerHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }
}
