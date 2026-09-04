package com.isameldev.skybook.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> erreurGlobal(IllegalArgumentException e){
        return ResponseEntity.badRequest().body("Attention : "+e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> notValidException(MethodArgumentNotValidException e){
        String messagePropre = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body("Attention : "+messagePropre);
    }
}
