package com.laspalmas.api.service.impl;

import com.laspalmas.api.model.Usuario;

import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.security.JwtUtil;
import com.laspalmas.api.service.AuthService;
import com.laspalmas.api.service.CorreoService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    private final CorreoService CorreoService;

    private final JwtUtil jwtUtil;

   
    @Override
    public String registrar(Usuario usuario) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreoOrNumeroCelular(usuario.getCorreo(), usuario.getNumeroCelular());
        if (usuarioExistente
        .isPresent()) {
           Usuario existente = usuarioExistente.get();
     if (existente.isVerified()) {
            throw new RuntimeException("El usuario ya existe y está verificado");
        } else if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
            // Regenerar token de verificación con UUID si tiene correo
            String verificationToken = UUID.randomUUID().toString();
            existente.setVerficationToken(verificationToken);
            existente.setTokenExpiry(LocalDateTime.now().plusHours(24));
            usuarioRepository.save(existente);

            // Enviar correo de verificación
            CorreoService.sendVerificationEmail(existente.getCorreo(), verificationToken);
            return "Correo de verificación reenviado. Revisa tu bandeja de entrada";
        } else {
            // Usuario con solo número de celular, no requiere verificación
            return "El usuario ya existe pero no requiere verificación de correo";
        }

        }

          
      // Nuevo registro
    usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

    if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
        String verificationToken = UUID.randomUUID().toString();
        usuario.setVerficationToken(verificationToken);
        usuario.setTokenExpiry(LocalDateTime.now().plusHours(24));
        // Enviar correo de verificación
        CorreoService.sendVerificationEmail(usuario.getCorreo(), verificationToken);
    } else {
        // Si no hay correo, no se requiere verificación
        usuario.setVerified(true);
    }

    usuarioRepository.save(usuario);

        return "¡Registro exitoso! Por favor, verifique su correo electrónico.";
    }

    @Override
   public Map<String, String> login(String credencial, String contraseña) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credencial, contraseña)
    );

    String rol = auth.getAuthorities().iterator().next().getAuthority();
    String token = jwtUtil.generateToken(credencial, rol);

      return Map.of("token", "Bearer " + token, "rol", rol);
}


}