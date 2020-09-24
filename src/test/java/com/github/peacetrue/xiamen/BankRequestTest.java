package com.github.peacetrue.xiamen;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author : xiayx
 * @since : 2020-09-23 22:27
 **/
@SuppressWarnings("unchecked")
class BankRequestTest {

    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<service>\n" +
            "    <SYS_HEAD>\n" +
            "        <SERVICE_CODE>order</SERVICE_CODE>\n" +
            "        <SERVICE_SCENE>get</SERVICE_SCENE>\n" +
            "        <CONSUMER_ID>consumerId</CONSUMER_ID>\n" +
            "    </SYS_HEAD>\n" +
            "    <APP_HEAD>\n" +
            "        <BRANCH_ID>branchId</BRANCH_ID>\n" +
            "        <USER_ID>userId</USER_ID>\n" +
            "    </APP_HEAD>\n" +
            "    <BODY>\n" +
            "        <id>1</id>\n" +
            "    </BODY>\n" +
            "</service>";

    public static BankRequest<OrderGet> getOrderGetBankRequest() {
        return getOrderGetBankRequest(1L);
    }

    public static BankRequest<OrderGet> getOrderGetBankRequest(Long id) {
        BankRequest<OrderGet> bankRequest = new BankRequest<>();
        bankRequest.setSysHead(new BankRequest.BankSysHead("order", "get", "consumerId"));
        bankRequest.setAppHead(new BankRequest.BankAppHead("branchId", "userId"));
        bankRequest.setBody(new OrderGet(id));
        return bankRequest;
    }

    public static BankRequestResolverImpl getBankRequestResolver() {
        BankRequestResolverImpl bankRequestResolver = new BankRequestResolverImpl();
        bankRequestResolver.getBodyClasses().put("orderget", OrderGet.class);
        return bankRequestResolver;
    }


    public static BankResponseResolverImpl getBankResponseResolver() {
        BankResponseResolverImpl bankResponseResolver = new BankResponseResolverImpl();
        bankResponseResolver.getBodyClasses().put("orderget", OrderVO.class);
        return bankResponseResolver;
    }


    @Test
    void marshal() throws Exception {
        BankRequest bankRequest = getOrderGetBankRequest();

        JAXBContext jaxbContext = JAXBContext.newInstance(BankRequest.class, OrderGet.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Object element = new JAXBElement<>(new QName("service"), BankRequest.class, bankRequest);
        StringWriter writer = new StringWriter();
        marshaller.marshal(element, writer);
        this.xml = writer.toString();
        System.out.println(this.xml);

        marshaller = jaxbContext.createMarshaller();
        element = new JAXBElement<>(new QName("BODY"), OrderGet.class, (OrderGet) bankRequest.getBody());
//        marshaller.marshal(element,new Node());
    }


    @Test
    void unmarshal() throws Exception {
//        marshal();
        JAXBContext jaxbContext = JAXBContext.newInstance(BankRequest.class, OrderGet.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<BankRequest> element = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), BankRequest.class);
        BankRequest bankRequest = element.getValue();
        System.out.println(bankRequest);
        if (bankRequest.getBody() instanceof Node) {
            JAXBElement<OrderGet> orderGetElement = unmarshaller.unmarshal((Node) bankRequest.getBody(), OrderGet.class);
            bankRequest.setBody(orderGetElement.getValue());
        }
        System.out.println(bankRequest);
    }
}
