package com.laspalmas.api.controller;


import com.laspalmas.api.dto.UsuarioDTO;
import com.laspalmas.api.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

 
    private final UsuarioService usuarioService;

    @GetMapping("/con-pedidos")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosConPedidos() {
          return ResponseEntity.ok(usuarioService.obtenerUsuariosConPedidos());
    }
}
