package com.laspalmas.api.controller;

import com.laspalmas.api.model.enums.Rol;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.security.JwtUtil;
import com.laspalmas.api.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2UserService oAuth2UserService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login/success")
    public Map<String, String> loginSuccess(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            OAuth2AuthenticationToken authentication
    ) {
        String provider = authentication.getAuthorizedClientRegistrationId(); // "google" o "facebook"
        Usuario usuario = oAuth2UserService.procesarUsuario(provider, oAuth2User);

        // Generar JWT con tu clase JwtUtil
        String token = jwtUtil.generateToken(usuario.getCorreo(),Rol.USER.name());

        return Map.of(
                "jwt", token,
                "usuario", usuario.getNombre(),
                "correo", usuario.getCorreo()
        );
    }

    @GetMapping("/login/failed")
    public Map<String, String> loginFailed() {
        return Map.of(
                "error", "No se pudo autenticar el usuario"
        );
    }
}
