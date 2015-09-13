package io.paymenthighway.example.controller;

import io.paymenthighway.FormContainer;
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
public class FormAddCardController extends PaymentHighwayController {

  private static final String baseUri = "/add_card/";
  private static final String successUri = baseUri + "success";
  private static final String failureUri = baseUri + "failure";
  private static final String cancelUri = baseUri + "cancel";


  @RequestMapping(value="/add_card", method=RequestMethod.GET)
  public String showForm(HttpServletRequest request, Model model) {

    String language = "EN";

    String serverPath = getServerPath(request);

    FormContainer formContainer = formBuilder.generateAddCardParameters(
            serverPath + successUri, serverPath + failureUri, serverPath + cancelUri, language);

    model.addAttribute("action", formContainer.getAction());
    model.addAttribute("method", formContainer.getMethod());
    model.addAttribute("fields", formContainer.getFields());

    System.out.println("Initialized form with request-id:" + formContainer.getRequestId());

    return "form";
  }

  @RequestMapping(value="/add_card/success", method=RequestMethod.GET)
  public String success(@RequestParam Map<String,String> requestParams, Model model) throws Exception {

    validateFormRedirection(requestParams);

    UUID tokenizationId = UUID.fromString(requestParams.get("sph-tokenization-id"));

    TokenizationResponse response = paymentApi.tokenize(tokenizationId);

    if (response.getResult().getCode().equals(RESPONSE_CODE_OK)) {
      model.addAttribute("card", response.getCard());
      model.addAttribute("cardToken", response.getCardToken());
      return "add_card_success";
    } else {
      model.addAttribute("result", response.getResult());
      return "transaction_failed";
    }
  }

  @RequestMapping(value="/add_card/failure", method=RequestMethod.GET)
  public String failure(@RequestParam Map<String,String> requestParams, Model model) throws Exception {

    String signatureExplanation = getSignatureDescription(requestParams);

    model.addAttribute("errorMessage", String.format("Signature description: %s, sph-failure: %s",
            signatureExplanation,
            requestParams.get("sph-failure"))
    );

    return "fatal_error";
  }

  @RequestMapping(value="/add_card/cancel", method=RequestMethod.GET)
  public String cancel(@RequestParam Map<String,String> requestParams) throws Exception {
    validateFormRedirection(requestParams);
    return "cancel";
  }
}
