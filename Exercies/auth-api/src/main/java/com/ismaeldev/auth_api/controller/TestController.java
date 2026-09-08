package com.ismaeldev.auth_api.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secret")
public class TestController {

    @GetMapping
    public ResponseEntity<String> salutation(){
        return ResponseEntity.ok("Bravo !");
    }
}
