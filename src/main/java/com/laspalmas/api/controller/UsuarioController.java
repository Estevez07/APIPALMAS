package com.laspalmas.api.controller;


import com.laspalmas.api.model.UsuarioDTO;
import com.laspalmas.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/con-pedidos")
    public List<UsuarioDTO> obtenerUsuariosConPedidos() {
        return usuarioService.obtenerUsuariosConPedidos();
    }
}
