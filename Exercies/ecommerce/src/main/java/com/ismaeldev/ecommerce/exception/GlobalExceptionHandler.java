package com.ismaeldev.ecommerce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> gererLesErreursMetiers(IllegalArgumentException e){
        return ResponseEntity.badRequest().body("Erreur : "+e.getMessage());
    }
}
