package io.paymenthighway.example.controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@ControllerAdvice
public class ErrorController {

  @ExceptionHandler(value = {Exception.class, RuntimeException.class})
  public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
    ModelAndView mav = new ModelAndView("fatal_error");
    mav.addObject("errorMessage", e.getMessage());
    return mav;
  }
}