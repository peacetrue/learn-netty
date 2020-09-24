package com.github.peacetrue.xiamen;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author : xiayx
 * @since : 2020-09-23 12:19
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class BankRequest<Body> {

    @XmlElement(name = "SYS_HEAD")
    private BankSysHead sysHead;
    @XmlElement(name = "APP_HEAD")
    private BankAppHead appHead;
    @XmlElement(name = "BODY")
    private Body body;

    public BankRequest(Body body) {
        this.body = body;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BankSysHead {
        @XmlElement(name = "SERVICE_CODE")
        private String serviceCode;
        @XmlElement(name = "SERVICE_SCENE")
        private String serviceScene;
        @XmlElement(name = "CONSUMER_ID")
        private String consumerId;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BankAppHead {
        @XmlElement(name = "BRANCH_ID")
        private String branchId;
        @XmlElement(name = "USER_ID")
        private String userId;
    }

}
