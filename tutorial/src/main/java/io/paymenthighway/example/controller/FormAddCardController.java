package io.paymenthighway.example.controller;

import io.paymenthighway.FormContainer;
import io.paymenthighway.example.utils.Sorting;
import io.paymenthighway.model.response.TokenizationResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;


@Controller
public class FormAddCardController extends PaymentHighway {

  private static final String baseUri = "/add_card";
  private static final String successUri = baseUri + "/success";
  private static final String failureUri = baseUri + "/failure";
  private static final String cancelUri = baseUri + "/cancel";


  @RequestMapping(value=baseUri, method=RequestMethod.GET)
  public String showForm(HttpServletRequest request, Model model) {

    String serverPath = getServerPath(request);
    String language = "EN";

    /**
     * Use the formBuilder to generate add card formContainer.
     * You may use the serverPath concatenated with the success, failure and cancel URIs.
     */
    FormContainer formContainer = null;

    /**
     * Use model.addAttribute("attributeName", value) method to inject
     * "action", "method" and "fields" to the template from the formContainer.
     */


    // It is also recommended to log and/or store the request ID (formContainer.getRequestId)

    // These are just auxiliary attributes for displaying the authentication string
    model.addAttribute("byKeyComparator", Sorting.getByKeyComparator());
    model.addAttribute("serviceUrl", settings.getServiceUrl());


    return "form";
  }

  @RequestMapping(value=successUri, method=RequestMethod.GET)
  public String success(@RequestParam Map<String,String> requestParams, Model model) throws Exception {

    unimplemented("success");

    /**
     * Use the SecureSigner to validate form redirection.
     * The redirection GET request includes a bunch of "sph-" parameters in addition to the signature.
     * It is important to validate signature using the parameters and the shared secret to prevent tampering.
     * SecureSigner can do that for you.
     */


    /**
     * Get "sph-tokenization-id" from request parameters.
     * You may use UUID.fromString(string) to convert the string into UUID.
     */
    UUID tokenizationId = null;

    /**
     * Make a tokenize server-to-server request using the PaymentApi in order to get the actual card token
     * or an error result code if the card was rejected.
     */
    TokenizationResponse response = null;

    if (response.getResult().getCode().equals(RESULT_CODE_OK)) {
      model.addAttribute("card", response.getCard());
      model.addAttribute("cardToken", response.getCardToken());
      return "add_card_success";
    } else {
      model.addAttribute("result", response.getResult());
      return "transaction_failed";
    }
  }

  /**
   * The user is redirected to the failure URL in case of a fatal error.
   * If for example the credentials are incorrect, the response signature could not be calculated and thus is missing.
   */
  @RequestMapping(value=failureUri, method=RequestMethod.GET)
  public String failure(@RequestParam Map<String,String> requestParams, Model model) throws Exception {

    String signatureExplanation = getSignatureDescription(requestParams);

    model.addAttribute("errorMessage", String.format("Signature description: %s, sph-failure: %s",
            signatureExplanation,
            requestParams.get("sph-failure"))
    );

    return "fatal_error";
  }

  @RequestMapping(value=cancelUri, method=RequestMethod.GET)
  public String cancel(@RequestParam Map<String,String> requestParams) throws Exception {
    validateFormRedirection(requestParams);
    return "cancel";
  }

  @RequestMapping(value=baseUri, method=RequestMethod.POST)
  public String failure(Model model) throws Exception {

    model.addAttribute("errorMessage", "Check your \"action\" parameter in the " + getClass() + "!");

    return "fatal_error";
  }
}
