package com.laspalmas.api.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class OAuth2Controller {



    @GetMapping("/login/failed")
      public ResponseEntity<?> loginFailed() {
      return (ResponseEntity<?>) ResponseEntity.badRequest();
    }
}
