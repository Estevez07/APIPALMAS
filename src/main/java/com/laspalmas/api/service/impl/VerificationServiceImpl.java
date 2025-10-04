package com.laspalmas.api.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.model.enums.TokenTipo;
import com.laspalmas.api.repository.TokenRepository;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.service.VerificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService{
private final UsuarioRepository usuarioRepository;
private final TokenRepository tokenRepository;
 private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    @Modifying
    public String verificacionOTP(String correo, String otp) {
         Optional<Usuario> existe = usuarioRepository.findByCorreo(correo);
        
          if (existe.isEmpty()) {
       throw new RuntimeException("Usuario no encontrado");
    }

    Usuario user = existe.get();

    Optional<TokenUsuario> tokenOpt = user.getTokens().stream()
                .filter(t -> t.getTipo() == TokenTipo.VERIFICACION)
                .findFirst();

        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("No existe un codigo de verificación activo");
        }

       TokenUsuario token = tokenOpt.get();
       
        // Validar OTP y expiración
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
          tokenRepository.deleteByUsuarioAndTipo(user, TokenTipo.VERIFICACION);
            throw new RuntimeException("Código expirado");
        }

     if (!token.getToken().equals(otp)) {
            throw new RuntimeException("Código incorrecto");
        }
       user.setVerified(true);
         // Eliminar token después de verificar
        
       tokenRepository.deleteByUsuarioAndTipo(user, TokenTipo.VERIFICACION);
       usuarioRepository.save(user);  
        
        return "Correo electrónico verificado exitosamente!";
    }





    @Override
    @Transactional
    public String verificacionOTPPassword(String correo, String otp, String newPassword) {
        Optional<Usuario> existe = usuarioRepository.findByCorreo(correo);
        
          if (existe.isEmpty()) {
       throw new RuntimeException("Usuario no encontrado");
    }

    Usuario user = existe.get();
    
   Optional<TokenUsuario> tokenOpt = user.getTokens().stream()
                .filter(t -> t.getTipo() == TokenTipo.RESET)
                .findFirst();

        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("No existe un Código de recuperación activo");
        }

        TokenUsuario token = tokenOpt.get();
   
     // Validar expiración
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
       
         tokenRepository.deleteByUsuarioAndTipo(user, TokenTipo.RESET);
          usuarioRepository.save(user);
            throw new RuntimeException("Código expirado");
        }

        if (!token.getToken().equals(otp)) {
            throw new RuntimeException("Código incorrecto");
        }

        // Cambiar contraseña
        user.setContraseña(passwordEncoder.encode(newPassword));

        // Eliminar token de recuperación
   
    tokenRepository.deleteByUsuarioAndTipo(user, TokenTipo.RESET);
        usuarioRepository.save(user);
  return "has cambiado tu contraseña exitosamente!";
    }
    
}
