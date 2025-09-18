package com.laspalmas.api.service;

import com.laspalmas.api.model.UsuarioDTO;
import com.laspalmas.api.mapper.UsuarioMapper;

import com.laspalmas.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

   
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")

     public List<UsuarioDTO> obtenerUsuariosConPedidos() {
         return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO) 
                .toList();
    }
}
