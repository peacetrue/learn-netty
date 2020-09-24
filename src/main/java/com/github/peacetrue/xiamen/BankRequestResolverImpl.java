package com.github.peacetrue.xiamen;

import lombok.Data;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiayx
 * @since : 2020-09-23 23:39
 **/
@Data
public class BankRequestResolverImpl implements BankRequestResolver {

    private Map<String, Class<?>> bodyClasses = new HashMap<>();

    @Override
    public Object resolveBody(BankRequest<?> bankRequest) throws Exception {
        BankRequest.BankSysHead sysHead = bankRequest.getSysHead();
        String id = sysHead.getServiceCode() + sysHead.getServiceScene();
        Class<?> bodyClass = bodyClasses.get(id);
        JAXBContext jaxbContext = JAXBContext.newInstance(bodyClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Node requestBody = (Node) bankRequest.getBody();
        JAXBElement<?> element = unmarshaller.unmarshal(requestBody, bodyClass);
        return element.getValue();
    }
}
