package com.github.peacetrue.xiamen.server;

import com.github.peacetrue.xiamen.BankFrameDecoder;
import com.github.peacetrue.xiamen.BankFrameEncoder;
import com.github.peacetrue.xiamen.BankRequestCodec;
import com.github.peacetrue.xiamen.BankResponseCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : xiayx
 * @since : 2020-09-23 10:22
 **/
@Slf4j
@Getter
@Setter
public class BankServer {

    private int port = 8888;
    private ServerBootstrap serverBootstrap;
    private BankRequestCodec bankRequestCodec = new BankRequestCodec();
    private BankResponseCodec bankResponseCodec = new BankResponseCodec();
    private BankServerChannelHandler bankServerChannelHandler = new BankServerChannelHandler();
    private Channel channel;

    protected void init() {
        log.info("init server config");
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        // client.send: <Concreteness>BankRequest -> xml string -> prepend header -> byte[]
                        // server.recv: <Concreteness>BankRequest <- xml to ElementBankRequest <- remove header <- string with header <-byte[]
                        // server.send: <Concreteness>BankResponse -> xml string -> prepend header -> byte[]
                        // client.recv: <Concreteness>BankResponse <- xml to ElementBankResponse <- remove header <- string with header <- byte[]

                        socketChannel.pipeline().addLast(
                                new BankFrameDecoder(),
                                bankRequestCodec,
                                new BankFrameEncoder(),
                                bankResponseCodec,
                                bankServerChannelHandler
                        );
                    }
                });
    }

    protected void listen() {
        log.info("listen port");
        try {
            this.channel = this.serverBootstrap.bind(port).sync().channel();
            this.channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("listen error", e);
        }
    }

    public void start() {
        this.init();
        this.listen();
        this.serverBootstrap.config().group().shutdownGracefully();
        this.serverBootstrap.config().childGroup().shutdownGracefully();
    }

    public void close() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}
