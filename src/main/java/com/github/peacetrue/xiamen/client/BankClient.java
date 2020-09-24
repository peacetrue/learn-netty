package com.github.peacetrue.xiamen.client;

import com.github.peacetrue.xiamen.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;

/**
 * @author : xiayx
 * @since : 2020-09-23 10:22
 **/
@Slf4j
@Setter
@Getter
public class BankClient {

    private String host = "localhost";
    private int port = 8888;
    private Bootstrap bootstrap;
    private BankRequestCodec bankRequestCodec = new BankRequestCodec();
    private BankResponseCodec bankResponseCodec = new BankResponseCodec();
    private BankClientChannelHandler bankChannelHandler = new BankClientChannelHandler();
    private Channel channel;

    public void init() {
        log.info("init client config");
        this.bootstrap = new Bootstrap();
        bootstrap
                .group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        // client.send: <Concreteness>BankRequest -> xml string -> prepend header -> byte[]
                        // server.recv: <Concreteness>BankRequest <- xml to ElementBankRequest <- remove header <- string with header <-byte[]
                        // server.send: <Concreteness>BankResponse -> xml string -> prepend header -> byte[]
                        // client.recv: <Concreteness>BankResponse <- xml to ElementBankResponse <- remove header <- string with header <- byte[]
                        socketChannel.pipeline().addLast(
                                new BankFrameEncoder(),
                                bankRequestCodec,
                                new BankFrameDecoder(),
                                bankResponseCodec,
                                bankChannelHandler
                        );
                    }
                });
    }

    public void connect() {
        log.info("connect to server");
        try {
            this.channel = this.bootstrap.connect(new InetSocketAddress(host, port)).sync().channel();
        } catch (InterruptedException e) {
            log.error("connect server error", e);
            this.bootstrap.config().group().shutdownGracefully();
            return;
        }

        this.channel.closeFuture().addListener(future -> {
            Iterator<BiConsumer<BankResponse<?>, Throwable>> iterator
                    = bankChannelHandler.getCallbacks().values().iterator();
            while (iterator.hasNext()) {
                BiConsumer<BankResponse<?>, Throwable> next = iterator.next();
                next.accept(null, future.cause());
                iterator.remove();
            }
        });
    }

    public void start() {
        this.init();
        this.connect();
    }

    @SuppressWarnings("unchecked")
    public <T> BankResponse<T> invoke(BankRequest<?> request) {
        String requestId = UUID.randomUUID().toString().replaceAll("-", "");
        request.getSysHead().setConsumerId(requestId);
        log.info("send request: {}", request);

        try {
            this.channel.writeAndFlush(request).sync();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }


        Thread thread = Thread.currentThread();
        BankResponse[] bankResponses = new BankResponse[1];
        bankChannelHandler.getCallbacks().put(requestId, (bankResponse, throwable) -> {
            bankResponses[0] = bankResponse;
            if (bankResponse != null && !bankResponse.getSysHead().getConsumerId().equals(requestId)) {
                throw new IllegalStateException("response mismatch request");
            }
            if (throwable != null) log.error("request error", throwable);
            LockSupport.unpark(thread);
        });
        LockSupport.park(this);
        return bankResponses[0];
    }

    /*public <T> T invoke(BankRequest request, Class<T> responseType) {
        // object -> xml string -> prepend header -> byte[]
        Long requestId = UUID.randomUUID().getLeastSignificantBits();
        request.setRequestId(requestId);
        this.channel.writeAndFlush(request).sync();
        return (T) values.get(requestId).get();
    }*/

    public void close() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }

}
