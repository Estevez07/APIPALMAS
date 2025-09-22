package com.laspalmas.api.service.impl;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Rol;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.security.JwtUtil;
import com.laspalmas.api.service.AuthService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    public String registrar(Usuario usuario) {

        
        if (usuarioRepository.findByCorreoOrNumeroCelular(usuario.getCorreo(), usuario.getNumeroCelular())
        .isPresent()) {
             throw new RuntimeException("El correo o número de celular ya está registrado");
        }
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuario.setRol(Rol.USER);
        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
    }

    @Override
   public Map<String, String> login(String credencial, String contraseña) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credencial, contraseña)
    );

    String rol = auth.getAuthorities().iterator().next().getAuthority();
    String token = jwtUtil.generateToken(credencial, rol);

    Map<String, String> response = new HashMap<>();
    response.put("token", "Bearer " + token);
    response.put("rol", rol);
    return response;
}

}