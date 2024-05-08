package cn.edu.sustech.ces.service.alipay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AlipayConfig {

    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.appPrivateKey}")
    private String appPrivateKey;
    @Value("${alipay.publicKey}")
    private String alipayPublicKey;
    @Value("${alipay.gatewayUrl}")
    private String alipayGatewayUrl;
    @Value("${alipay.notifyUrl}")
    private String alipayNotifyUrl;
    @Value("${alipay.returnUrl}")
    private String alipayReturnUrl;

    private final String format="json";
    private final String charset="UTF-8";
    private final String signType="RSA2";

}
