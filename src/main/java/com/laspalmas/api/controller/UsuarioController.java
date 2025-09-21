package com.laspalmas.api.controller;


import com.laspalmas.api.dto.UsuarioDTO;
import com.laspalmas.api.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

 
    private final UsuarioService usuarioService;

    @GetMapping("/con-pedidos")
    public List<UsuarioDTO> obtenerUsuariosConPedidos() {
        return usuarioService.obtenerUsuariosConPedidos();
    }
}
