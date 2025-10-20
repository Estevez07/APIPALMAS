package com.laspalmas.api.service.impl;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.ProviderInfo;
import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.model.enums.Provider;
import com.laspalmas.api.model.enums.TokenTipo;
import com.laspalmas.api.repository.TokenRepository;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.security.JwtUtil;
import com.laspalmas.api.service.AuthService;
import com.laspalmas.api.service.CorreoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final CorreoService correoService;

    private final JwtUtil jwtUtil;

   
    @Override
    @Transactional
    public String registrar(Usuario usuario) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreoOrNumeroCelular(usuario.getCorreo(), usuario.getNumeroCelular());
        if (usuarioExistente
        .isPresent()) {
           Usuario existente = usuarioExistente.get();
     if (existente.isVerified()) {
            throw new RuntimeException("El usuario ya existe y está verificado");
        } else if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
            
    List<TokenUsuario> tokensExistentes = tokenRepository.findByUsuarioAndTipo(existente, TokenTipo.VERIFICACION);

   
    if (!tokensExistentes.isEmpty()) {
        tokenRepository.deleteAll(tokensExistentes);
    }
                String otp = generateOtp();

saveTokenUser(otp,existente,LocalDateTime.now().plusMinutes(10),TokenTipo.VERIFICACION);

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
       // Asignar provider LOCAL por defecto
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setProvider(Provider.LOCAL);
        providerInfo.setUsuario(usuario);
        usuario.setProviderInfo(providerInfo);
        
  Usuario usuarioGuardado = usuarioRepository.save(usuario);
    if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
       
        String otp = generateOtp();

saveTokenUser(otp,usuarioGuardado,LocalDateTime.now().plusMinutes(10),TokenTipo.VERIFICACION);

        correoService.sendOtpEmail(usuarioGuardado.getCorreo(), otp);

        return "¡Registro exitoso! Por favor, verifique su correo electrónico.";
    } else {
        usuarioGuardado.setVerified(true);
        usuarioRepository.save(usuarioGuardado);
    }

    return "¡Registro exitoso!";

    }

    @Override
   public Map<String, String> login(String credencial, String contraseña) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credencial, contraseña)
    );

    String rol = auth.getAuthorities().iterator().next().getAuthority();
    String jwt = jwtUtil.generateToken(credencial, rol);

Usuario usuario = usuarioRepository.buscarPorCredencial(credencial)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


saveTokenUser(jwt,usuario,LocalDateTime.now().plusHours(48),TokenTipo.LOGIN);


      return Map.of("token", "Bearer " + jwt, "rol", rol);
}

@Override
@Transactional
public String recuperarPassword(String correo) {
     Usuario user = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El usuario no ha sido encontrado"));
     if (user.isVerified()) {

          List<TokenUsuario> tokensExistentes = tokenRepository.findByUsuarioAndTipo(user, TokenTipo.RESET);

   
    if (!tokensExistentes.isEmpty()) {
        tokenRepository.deleteAll(tokensExistentes);
    }
                String otp = generateOtp();

saveTokenUser(otp,user,LocalDateTime.now().plusMinutes(10),TokenTipo.RESET);
         
             correoService.sendForgotPasswordEmail(user.getCorreo(), otp);
        }else {
             throw new RuntimeException("El usuario no esta verificado");
        }
             return "Verifica tu correo para poder recuperar tu contraseña";
}
  

private String generateOtp() {
    Random random = new Random();
    int otp = 100000 + random.nextInt(900000); // genera un número entre 100000 y 999999
    return String.valueOf(otp);
}

private void saveTokenUser(String token,Usuario user,LocalDateTime expiryDate,TokenTipo tipo){

        TokenUsuario tokenVerificacion = new TokenUsuario();
        tokenVerificacion.setToken(token);
        tokenVerificacion.setExpiryDate(expiryDate);
        tokenVerificacion.setTipo(tipo);
        tokenVerificacion.setUsuario(user);
        if(tipo == TokenTipo.LOGIN){
        tokenVerificacion.setLoggedOut(false);
        }
        tokenRepository.save(tokenVerificacion);
}
}