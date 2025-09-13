package com.laspalmas.api.service;

import com.laspalmas.api.model.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> obtenerUsuariosConPedidos();
}