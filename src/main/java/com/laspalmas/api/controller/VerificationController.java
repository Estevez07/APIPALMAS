package com.laspalmas.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.service.VerificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/verify")
public class VerificationController {
    private final VerificationService verificationService;


     
    @PostMapping("/register")
    public ResponseEntity<?> verifyOtp(@RequestBody Usuario usuario) {
         String otp = usuario.getTokens().get(0).getToken();
    return ResponseEntity.status(HttpStatus.CREATED).body(verificationService.verificacionOTP(usuario.getCorreo(),otp));

   } 
    @PostMapping("/register/forgot_password")
    public ResponseEntity<?> resetPassword(@RequestBody Usuario usuario) {
         String otp = usuario.getTokens().get(0).getToken();
    return ResponseEntity.status(HttpStatus.CREATED).body(verificationService.verificacionOTPPassword(usuario.getCorreo(),otp,usuario.getContraseña()));
   


    } 
}