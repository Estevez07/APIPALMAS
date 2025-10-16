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
// El principal sera Correo, si no tiene tomamos su numero de celular
    @Override
    public UserDetails loadUserByUsername(String credencial) throws UsernameNotFoundException {
         Usuario usuario = usuarioRepository.buscarPorCredencial(credencial)
            .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));

    
    String username = usuario.getCorreo() != null ? usuario.getCorreo() : usuario.getNumeroCelular();
    String password = usuario.getContraseña() != null ? usuario.getContraseña() : "";
        return new User(username ,password,
                List.of(new SimpleGrantedAuthority(usuario.getRol().toString())));
    }
}
