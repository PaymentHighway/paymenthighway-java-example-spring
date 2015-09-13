package io.paymenthighway.example.controller;

import io.paymenthighway.example.PaymentHighwaySettings;
import io.paymenthighway.FormBuilder;
import io.paymenthighway.PaymentAPI;
import io.paymenthighway.security.SecureSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

abstract public class PaymentHighwayController extends WebMvcConfigurerAdapter {

  /**
   * The Form builder is a helper class to build a {@link io.paymenthighway.FormContainer}r for a specific Form.
   * The FormContainer can be used to generate the required fields, method to initiate the Payment Highway form.
   */
  protected FormBuilder formBuilder;

  /**
   * Payment API class provides server-to-server interface to the Payment Highway.
   * This includes commands such as init and debit transactions, reverts, as well as commit and tokenize requests to the
   * results of the form redirects.
   */
  protected PaymentAPI paymentApi;

  @Autowired
  private PaymentHighwaySettings settings;

  private SecureSigner secureSigner;

  protected final static String RESPONSE_CODE_OK = "100";

  @PostConstruct
  public void init() {

    String formMethod = "POST";

    formBuilder = new FormBuilder(
            formMethod, settings.getSignatureKeyId(), settings.getSignatureSecret(),
            settings.getAccount(), settings.getMerchant(),
            settings.getServiceUrl()
    );

    paymentApi = new PaymentAPI(
            settings.getServiceUrl(), settings.getSignatureKeyId(),
            settings.getSignatureSecret(), settings.getAccount(),
            settings.getMerchant()
    );


    secureSigner = new SecureSigner(settings.getSignatureKeyId(), settings.getSignatureSecret());
  }

  public String getServerPath(HttpServletRequest request) {
    return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
  }

  /**
   * Returns a description of what is going on with the signature.
   * Signature is not returned during a failure form redirection if the credentials are invalid as it could not
   * be calculated!
   * @param requestParams Parameters received in the form redirection
   * @return signatureDescription
   */
  protected String getSignatureDescription(Map<String, String> requestParams) throws Exception {

    if (requestParams.get("signature") != null) {
      try {
        validateFormRedirection(requestParams);
        return "A valid signature found.";
      } catch (Exception e) {
        return "An invalid signature found.";
      }
    } else {
      return "Signature not found, please check your credentials!";
    }
  }

  protected void validateFormRedirection(Map<String, String> requestParams) throws Exception {
    if ( ! secureSigner.validateFormRedirect(requestParams)) {
      throw new Exception("Invalid signature! Shut down everything!");
    }
  }
}
