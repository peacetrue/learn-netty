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
public class BankRequestCodec extends MessageToMessageCodec<String, BankRequest<?>> {

    private BankRequestResolver bankRequestResolver;

    @Override
    protected void encode(ChannelHandlerContext ctx, BankRequest<?> msg, List<Object> out) throws Exception {
        log.info("step 1 of 4: encode msg from BankRequest to String");
        log.debug("msg(BankRequest): {}", msg);
        StringWriter writer = new StringWriter();
        Object element = new JAXBElement<>(new QName("service"), BankRequest.class, msg);
        JAXBContext.newInstance(msg.getClass(), msg.getBody().getClass()).createMarshaller().marshal(element, writer);
        String string = writer.toString();
        log.debug("msg(String): {}", string);
        out.add(string);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(BankRequest.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<BankRequest> element = unmarshaller.unmarshal(new StreamSource(new StringReader(msg)), BankRequest.class);
        BankRequest bankRequest = element.getValue();
        log.debug("decode msg(xml string)[{}] to BankRequest<Element>[{}]", msg, bankRequest);
        Object bankRequestBody = bankRequestResolver.resolveBody(bankRequest);
        bankRequest.setBody(bankRequestBody);
        out.add(bankRequest);
    }
}
