package io.paymenthighway.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:paymenthighway.properties")
public class PaymentHighwaySettings {

    @Value("${paymenthighway.account}")
    private String account;

    @Value("${paymenthighway.merchant}")
    private String merchant;

    @Value("${paymenthighway.serviceUrl}")
    private String serviceUrl;

    @Value("${paymenthighway.signatureKeyId}")
    private String signatureKeyId;

    @Value("${paymenthighway.signatureSecret}")
    private String signatureSecret;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getSignatureKeyId() {
        return signatureKeyId;
    }

    public void setSignatureKeyId(String signatureKeyId) {
        this.signatureKeyId = signatureKeyId;
    }

    public String getSignatureSecret() {
        return signatureSecret;
    }

    public void setSignatureSecret(String signatureSecret) {
        this.signatureSecret = signatureSecret;
    }
}