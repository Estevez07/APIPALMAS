package com.laspalmas.api.service.impl;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsServiceImpl implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String numeroCelular) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));

        return new User(usuario.getNumeroCelular(), usuario.getContraseña(),
                List.of(new SimpleGrantedAuthority(usuario.getRol().toString())));
    }
}
