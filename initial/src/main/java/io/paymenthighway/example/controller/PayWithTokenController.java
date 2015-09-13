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
public class PayWithTokenController extends PaymentHighway {

  @RequestMapping(value="/pay_with_token", method=RequestMethod.GET)
  public String payWithToken(@RequestParam("token") String requestToken, Model model) throws IOException {

    try {

      // Create a new Token object using the request parameter
      Token token = null;

      // Create a new TransactionRequest using the token
      TransactionRequest request = null;

      // Initialize a server-to-server transaction using the PaymentAPI
      // Get the transaction ID from the response
      UUID transactionId = null;

      // Use the PaymentAPI to make a debit transaction using the new transaction ID
      TransactionResponse response = null;


      if (response.getResult().getCode().equals(RESULT_CODE_OK)) {
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
