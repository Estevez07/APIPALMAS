package com.laspalmas.api.service;

import com.laspalmas.api.model.UsuarioDTO;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UsuarioDTO> obtenerUsuariosConPedidos() {
        List<Usuario> usuarios = usuarioRepository.findUsuariosConPedidos();
        return usuarios.stream()
                .map(u -> new UsuarioDTO(u.getNombre(), u.getNumeroCelular()))
                .collect(Collectors.toList());
    }
}
