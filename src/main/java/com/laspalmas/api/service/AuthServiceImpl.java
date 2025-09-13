package com.laspalmas.api.service;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String registrar(Usuario usuario) {
        if (usuarioRepository.findByNumeroCelular(usuario.getNumeroCelular()).isPresent()) {
            throw new RuntimeException("El número ya está registrado");
        }

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuario.setRol("USER");
        usuarioRepository.save(usuario);
        return "Usuario registrado correctamente";
    }

    @Override
   public Map<String, String> login(String numeroCelular, String contraseña) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(numeroCelular, contraseña)
    );

    String rol = auth.getAuthorities().iterator().next().getAuthority();
    String token = jwtUtil.generateToken(numeroCelular, rol);

    Map<String, String> response = new HashMap<>();
    response.put("token", "Bearer " + token);
    response.put("rol", rol);
    return response;
}

}