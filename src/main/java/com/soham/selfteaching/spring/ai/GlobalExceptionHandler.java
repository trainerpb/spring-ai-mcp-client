package com.soham.selfteaching.spring.ai;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.OK)
    public String handleNotFound(IllegalStateException ex, HttpServletRequest request) {
        return """
         Sorry! I seem to have trouble understanding your question. Please rephrase it or ask something else related to Ideal College of Engineering.
        This could be due to a temporary issue or a question that is outside the scope of my knowledge. I'm here to help with any information about Ideal College of Engineering, its courses, admission process, campus facilities, and other related topics. Please feel free to ask another question!
        I am designed NOT to provide incorrect information ; instead I will politely decline to answer questions that are not relevant to the Institution. Please rephrase your question specifically.
        """;

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public String  handleValidation(Exception ex, HttpServletRequest request) {
        return  """
         Sorry! I seem to have trouble understanding your question. Please rephrase it or ask something else related to Ideal College of Engineering.
        This could be due to a temporary issue or a question that is outside the scope of my knowledge. I'm here to help with any information about Ideal College of Engineering, its courses, admission process, campus facilities, and other related topics. Please feel free to ask another question!
        I am designed NOT to provide incorrect information ; instead I will politely decline to answer questions that are not relevant to the Institution. Please rephrase your question specifically.
        """;
    }
}

