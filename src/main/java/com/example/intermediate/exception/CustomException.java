package com.example.intermediate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomException {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(Exception.class)
    public String LoginException(Exception e, Model model) {
        model.addAttribute("Expired JWT token", e);
        return "로그인이 필요합니다.";
    }
}
