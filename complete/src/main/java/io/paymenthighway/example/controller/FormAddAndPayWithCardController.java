package io.paymenthighway.example.controller;

import io.paymenthighway.FormContainer;
import io.paymenthighway.example.utils.Sorting;
import io.paymenthighway.model.response.CommitTransactionResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@Controller
public class FormAddAndPayWithCardController extends PaymentHighway {

  private static final String baseUri = "/add_and_pay_with_card";
  private static final String successUri = baseUri + "/success";
  private static final String failureUri = baseUri + "/failure";
  private static final String cancelUri = baseUri + "/cancel";


  @RequestMapping(value=baseUri, method=RequestMethod.GET)
  public String showForm(HttpServletRequest request, Model model) {
    Long amount = 1990L;
    String currency = "EUR";
    String orderId = "1000123A";
    String description = "A Box of Dreams. 19,90€";

    String language = "EN";
    String serverPath = getServerPath(request);

    FormContainer formContainer = formBuilder.generateAddCardAndPaymentParameters(serverPath + successUri,
            serverPath + failureUri, serverPath + cancelUri, language, Long.toString(amount), currency, orderId, description, null, null, null, false);

    model.addAttribute("action", formContainer.getAction());
    model.addAttribute("method", formContainer.getMethod());
    model.addAttribute("fields", formContainer.getFields());

    System.out.println("Initialized form with request-id:" + formContainer.getRequestId());

    // These are just auxiliary attributes for displaying the authentication string
    model.addAttribute("byKeyComparator", Sorting.getByKeyComparator());
    model.addAttribute("serviceUrl", settings.getServiceUrl());

    return "form";
  }

  @RequestMapping(value=successUri, method=RequestMethod.GET)
  public String success(@RequestParam Map<String,String> requestParams, Model model) throws Exception {

    validateFormRedirection(requestParams);

    UUID transactionId = UUID.fromString(requestParams.get("sph-transaction-id"));

    CommitTransactionResponse response = paymentApi.commitTransaction(transactionId, Long.toString(1990L), "EUR");

    if (response.getResult().getCode().equals(RESULT_CODE_OK)) {
      model.addAttribute("card", response.getCard());
      model.addAttribute("token", response.getCardToken() == null ? "None" : response.getCardToken().toString());
      return "add_and_pay_with_card_success";
    } else {
      model.addAttribute("result", response.getResult());
      return "transaction_failed";
    }
  }

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
}
