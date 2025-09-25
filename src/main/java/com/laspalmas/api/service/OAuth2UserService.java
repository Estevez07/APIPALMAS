package com.laspalmas.api.service;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Provider;
import com.laspalmas.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    public Usuario procesarUsuario(String providerStr, OAuth2User oAuth2User) {
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

        return usuarioRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario();
                    nuevo.setProvider(provider);
                    nuevo.setProviderId(providerId);
                    nuevo.setNombre(nombre);
                    nuevo.setApellidos(apellidos);
                    nuevo.setCorreo(correo);
                    // Para usuarios OAuth, fechaNac y contraseña pueden quedar nulas
                    return usuarioRepository.save(nuevo);
                });
    }
}
