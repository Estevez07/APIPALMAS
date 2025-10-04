package com.laspalmas.api.controller;

import com.laspalmas.api.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2UserService oAuth2UserService;
  

    @GetMapping("/login/success")
    public ResponseEntity<?> loginSuccess(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            OAuth2AuthenticationToken authentication
    ) {
        String provider = authentication.getAuthorizedClientRegistrationId(); // "google" o "facebook"
      
      return ResponseEntity.ok(oAuth2UserService.login(provider,oAuth2User));

    }

    @GetMapping("/login/failed")
      public ResponseEntity<?> loginFailed() {
      return (ResponseEntity<?>) ResponseEntity.badRequest();
    }
}
