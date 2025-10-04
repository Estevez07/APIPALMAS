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
import java.util.Random;


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

    private final CorreoService correoService;

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
             String otp = generateOtp();
             usuario.setVerificationToken(otp);
             usuario.setTokenExpiry(LocalDateTime.now().plusMinutes(10)); // caduca en 10 min
    
            usuarioRepository.save(existente);

            // Enviar de nuevo el correo de verificación
            correoService.sendOtpEmail(usuario.getCorreo(), otp);
            return "Correo de verificación reenviado. Revisa tu bandeja de entrada";
        } else {
            // Usuario con solo número de celular, no requiere verificación
            return "El usuario ya existe pero no requiere verificación de correo";
        }

        }

          
      // Nuevo registro
    usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

    if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
        String otp = generateOtp();
        usuario.setVerificationToken(otp);
        usuario.setTokenExpiry(LocalDateTime.now().plusMinutes(10)); // caduca en 10 min
        // Enviar correo de verificación
        correoService.sendOtpEmail(usuario.getCorreo(), otp);
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

private String generateOtp() {
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000); // genera un número entre 100000 y 999999
    return String.valueOf(otp);
}

}