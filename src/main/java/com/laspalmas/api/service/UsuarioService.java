package com.laspalmas.api.service;

import java.util.List;

import com.laspalmas.api.dto.UsuarioDTO;

public interface UsuarioService {
    List<UsuarioDTO> obtenerUsuariosConPedidos();
}