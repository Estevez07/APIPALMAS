package com.laspalmas.api.controller;


import com.laspalmas.api.constant.ApiPaths;
import com.laspalmas.api.dto.UsuarioDTO;
import com.laspalmas.api.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.USUARIOS)
public class UsuarioController {

 
    private final UsuarioService usuarioService;

    @GetMapping(ApiPaths.USUARIOS_CON_PEDIDOS)
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosConPedidos() {
          return ResponseEntity.ok(usuarioService.obtenerUsuariosConPedidos());
    }
}
