package com.laspalmas.api.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import java.util.Optional;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.laspalmas.api.model.ProviderInfo;
import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Provider;
import com.laspalmas.api.model.enums.Rol;
import com.laspalmas.api.model.enums.TokenTipo;
import com.laspalmas.api.repository.TokenRepository;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.security.JwtUtil;
import com.laspalmas.api.service.OAuth2UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService{
     private final UsuarioRepository usuarioRepository;
      private final TokenRepository tokenRepository;
     private final JwtUtil jwtUtil;
     
    public Map<String, String> login (String providerStr, OAuth2User oAuth2User) {
        
        Provider provider;
        String providerId;
        String nombre;
        String apellidos;
        String correo;

        if ("google".equals(providerStr)) {
            provider = Provider.GOOGLE;
            providerId = oAuth2User.getAttribute("sub");
            nombre = oAuth2User.getAttribute("given_name"); 
            apellidos = oAuth2User.getAttribute("family_name"); 
            correo = oAuth2User.getAttribute("email");
        } else if ("facebook".equals(providerStr)) {
            provider = Provider.FACEBOOK;
            providerId = oAuth2User.getAttribute("id");
            nombre = oAuth2User.getAttribute("first_name");
            apellidos = oAuth2User.getAttribute("last_name");
            correo = oAuth2User.getAttribute("email");
        } else {
            throw new IllegalArgumentException("Proveedor no soportado: " + providerStr);
        }

        
        Optional<Usuario> opt = usuarioRepository
                .findByProviderInfoProviderAndProviderInfoProviderId(provider, providerId);
        Usuario usuario;
        if (opt.isPresent()) {
            usuario = opt.get();
        } else {
        
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setCorreo(correo);
            usuario.setVerified(true);
            usuario.setRol(Rol.USER);
          


            ProviderInfo providerInfo = new ProviderInfo();
            providerInfo.setProvider(provider);
            providerInfo.setProviderId(providerId);
            providerInfo.setUsuario(usuario);
            usuario.setProviderInfo(providerInfo);

            usuario = usuarioRepository.save(usuario);
 }
     
      String jwt = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().name());
    
        
        TokenUsuario tokenLogin = new TokenUsuario();
        tokenLogin.setToken(jwt);
        tokenLogin.setTipo(TokenTipo.LOGIN);
        tokenLogin.setExpiryDate(LocalDateTime.now().plusHours(48));
        tokenLogin.setLoggedOut(false);
        tokenLogin.setUsuario(usuario);

        tokenRepository.save(tokenLogin);


          return Map.of(
                "jwt", "Bearer " + jwt,
                "usuario", usuario.getNombre() + (usuario.getApellidos() != null ? " " + usuario.getApellidos() : ""),
                "correo", usuario.getCorreo() == null ? "" : usuario.getCorreo()
        );
        }
    }

  
