package com.github.peacetrue.xiamen;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : xiayx
 * @since : 2020-09-23 11:09
 **/
@Getter
@Setter
@Slf4j
public class BankFrameEncoder extends MessageToByteEncoder<String> {

    private int headerLength = BankProtocol.HEADER_LENGTH;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, ByteBuf byteBuf) {
        log.info("step 2 of 4: encode msg from String[{}] to ByteBuf", msg.length());
        log.debug("msg(String): {}", msg);
        byte[] bodyBytes = msg.getBytes();
        String header = StringUtils.leftPad(String.valueOf(bodyBytes.length), headerLength, '0');
        byte[] headerBytes = header.getBytes();
        ByteBuf buffer = byteBuf.alloc().buffer(headerBytes.length + bodyBytes.length);
        buffer.writeBytes(headerBytes);
        buffer.writeBytes(bodyBytes);
        byteBuf.writeBytes(buffer);
        buffer.release();
    }
}
