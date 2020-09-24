package com.github.peacetrue.xiamen;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * @author : xiayx
 * @since : 2020-09-23 11:09
 **/
@Slf4j
@Setter
@Getter
@ChannelHandler.Sharable
public class BankResponseCodec extends MessageToMessageCodec<String, BankResponse<?>> {

    private BankResponseResolver bankResponseResolver = new BankResponseResolverImpl();

    @Override
    protected void encode(ChannelHandlerContext ctx, BankResponse<?> msg, List<Object> out) throws Exception {
        StringWriter writer = new StringWriter();
        Object element = new JAXBElement<>(new QName("service"), BankResponse.class, msg);
        JAXBContext.newInstance(msg.getClass(), msg.getBody().getClass())
                .createMarshaller().marshal(element, writer);
        log.debug("encode msg(BankResponse)[{}] to xml string[{}]", msg, writer.toString());
        out.add(writer.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        log.info("step 4 of 4: decode msg from String to BankResponse");
        log.debug("msg(String): {}", msg);
        JAXBContext jaxbContext = JAXBContext.newInstance(BankResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<?> element = unmarshaller.unmarshal(new StreamSource(new StringReader(msg)), BankResponse.class);
        BankResponse<Object> bankResponse = (BankResponse<Object>) element.getValue();
        Object bankResponseBody = bankResponseResolver.resolveBody(bankResponse);
        bankResponse.setBody(bankResponseBody);
        log.debug("msg(BankResponse): {}", bankResponse);
        out.add(bankResponse);
    }
}
