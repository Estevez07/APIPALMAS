package com.laspalmas.api.service;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String numeroCelular) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));

        return new User(usuario.getNumeroCelular(), usuario.getContraseña(),
                List.of(new SimpleGrantedAuthority(usuario.getRol())));
    }
}
