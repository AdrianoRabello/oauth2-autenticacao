package com.oauth2.autenticacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signin")
public class SigninController {

    @GetMapping
    public ResponseEntity index() {
        return ResponseEntity.ok().build();
    }
}
