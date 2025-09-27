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
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
       
        Optional<Usuario> existe = usuarioRepository.findByVerficationToken(token);
        
          if (existe.isEmpty()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token inválido o expirado!");
    }

    Usuario user = existe.get();

    // Validar expiración si existe tokenExpiry
    if (user.getTokenExpiry() != null && user.getTokenExpiry().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expirado!");
    }

        user = existe.get();
        user.setVerficationToken(null);
        user.setVerified(true);  
        user.setTokenExpiry(null);
        usuarioRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Correo electrónico verificado exitosamente!");
    } 
}