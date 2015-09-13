package io.paymenthighway.example.controller;

import io.paymenthighway.model.Token;
import io.paymenthighway.model.request.TransactionRequest;
import io.paymenthighway.model.response.TransactionResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;


@Controller
public class PayWithTokenController extends PaymentHighwayController {

  @RequestMapping(value="/pay_with_token", method=RequestMethod.GET)
  public String payWithToken(@RequestParam("token") String requestToken, Model model) throws IOException {

    try {
      Token token = new Token(requestToken);

      TransactionRequest request = new TransactionRequest(token, "1990", "EUR");

      UUID transactionId = paymentApi.initTransaction().getId();

      TransactionResponse response = paymentApi.debitTransaction(transactionId, request);

      if (response.getResult().getCode().equals(RESPONSE_CODE_OK)) {
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("token", requestToken);
        return "pay_with_token_success";
      } else {
        model.addAttribute("result", response.getResult());
        return "transaction_failed";
      }

    } catch(IllegalArgumentException e) {
      model.addAttribute("errorMessage", "Invalid token: " + e.getMessage());
      return "fatal_error";
    }
  }
}
