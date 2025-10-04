package com.laspalmas.api.controller;

import java.time.LocalDateTime;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    

  
    private final UsuarioRepository usuarioRepository;
    

     
    @GetMapping("/auth/register/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String correo, @RequestParam String otp) {
       
        Optional<Usuario> existe = usuarioRepository.findByCorreo(correo);
        
          if (existe.isEmpty()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuario no encontrado");
    }

    Usuario user = existe.get();

    // Validar expiración si existe tokenExpiry
    if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expirado!");
    }

     if (!user.getVerificationToken().equals(otp)) {
        return ResponseEntity.badRequest().body("Código incorrecto");
    }

        user.setVerificationToken(null);
        user.setVerified(true);  
        user.setTokenExpiry(null);
        usuarioRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Correo electrónico verificado exitosamente!");
    } 
}