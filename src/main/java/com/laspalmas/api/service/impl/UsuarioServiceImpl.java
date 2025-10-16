package com.laspalmas.api.service.impl;

import com.laspalmas.api.constant.Authorization;
import com.laspalmas.api.dto.UsuarioDTO;
import com.laspalmas.api.mapper.UsuarioMapper;

import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.service.UsuarioService;

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
    @PreAuthorize(Authorization.ADMIN)

     public List<UsuarioDTO> obtenerUsuariosConPedidos() {
         return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO) 
                .toList();
    }
}
