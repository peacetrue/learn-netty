package com.github.peacetrue.xiamen;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author : xiayx
 * @since : 2020-09-23 12:19
 **/
@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class BankResponse<Body> {

    @XmlElement(name = "SYS_HEAD")
    private BankSysHead sysHead;
    @XmlElement(name = "APP_HEAD")
    private BankAppHead appHead;
    @XmlElement(name = "BODY")
    private Body body;

    @Getter
    @Setter
    @ToString
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BankSysHead {
        @XmlElement(name = "SERVICE_CODE")
        private String serviceCode;
        @XmlElement(name = "SERVICE_SCENE")
        private String serviceScene;
        @XmlElement(name = "CONSUMER_ID")
        private String consumerId;
        @XmlElement(name = "RET_STATUS")
        private String retStatus;
        @XmlElement(name = "RET_MSG")
        private String retMsg;

        @XmlElementWrapper(name = "RET")
        @XmlElement(name = "struct")
        private List<Result> ret;

        @Getter
        @Setter
        @ToString
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Result {
            @XmlElement(name = "RET_CODE")
            private String retCode;
            @XmlElement(name = "RET_MSG")
            private String retMsg;
        }
    }

    @Getter
    @Setter
    @ToString
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BankAppHead {
        @XmlElement(name = "BRANCH_ID")
        private String branchId;
        @XmlElement(name = "USER_ID")
        private String userId;
    }

}
