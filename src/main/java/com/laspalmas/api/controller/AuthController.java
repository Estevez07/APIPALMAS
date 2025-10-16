package com.laspalmas.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laspalmas.api.constant.ApiPaths;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.AUTH)
public class AuthController {

   
    private final AuthService authService;

    @PostMapping(ApiPaths.REGISTER)
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
         return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(usuario));
    }

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        String credencial = usuario.getCorreo()!=null? usuario.getCorreo() : usuario.getNumeroCelular();
        return ResponseEntity.ok(authService.login(credencial, usuario.getContraseña()));
    }

    @PostMapping(ApiPaths.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody Usuario usuario) {
         return ResponseEntity.status(HttpStatus.CREATED).body(authService.recuperarPassword(usuario.getCorreo()));
    }

}
