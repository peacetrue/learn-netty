package com.github.peacetrue.learn.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;

/**
 * @author : xiayx
 * @since : 2020-09-21 11:25
 **/
@Setter
public class DiscardServer {

    private int port;
    private ChannelHandler channelHandler = new DiscardServerHandler();

    public DiscardServer(int port) {
        this.port = port;
    }

    EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            try {
                                ch.pipeline().addLast(channelHandler.getClass().newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() throws Exception{
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
