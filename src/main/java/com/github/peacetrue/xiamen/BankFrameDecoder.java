package com.github.peacetrue.xiamen;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : xiayx
 * @since : 2020-09-23 11:09
 **/
@Slf4j
@Setter
public class BankFrameDecoder extends ByteToMessageDecoder {

    private int headerLength = BankProtocol.HEADER_LENGTH;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        log.info("step 3 of 4: decode msg from ByteBuf to String");
        if (byteBuf.readableBytes() >= headerLength) {
            byte[] headerBytes = new byte[headerLength];
            byteBuf.getBytes(byteBuf.readerIndex(), headerBytes);
            String headerString = new String(headerBytes);
            log.debug("get header value: {}", headerString);
            int headerInteger = Integer.parseInt(headerString);
            if (byteBuf.readableBytes() >= headerLength + headerInteger) {
                byteBuf.readBytes(headerLength);
                byte[] contentBytes = new byte[headerInteger];
                byteBuf.readBytes(contentBytes);
                String body = new String(contentBytes, StandardCharsets.UTF_8);
                log.debug("get body value: {}", body);
                list.add(body);
            }
        }
    }
}
