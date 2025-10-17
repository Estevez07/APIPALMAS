package com.laspalmas.api.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.laspalmas.api.security.JwtFilter;
import com.laspalmas.api.service.impl.OAuth2UserServiceImpl;
import com.laspalmas.api.service.impl.UsuarioDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) 
@RequiredArgsConstructor
public class SecurityConfig {

      private final OAuth2UserServiceImpl oAuth2UserService;
      private final CustomOAuth2FailureHandler failureHandler;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter,
                                       UsuarioDetailsServiceImpl uds,
                                       CustomLogoutHandler customLogoutHandler) throws Exception {

    http.csrf(csrf -> csrf.disable())
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint((request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"No autorizado o token invalido\"}");
        })
    )    
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**", "/oauth2/**", "/login/**","/swagger-ui/**","/v3/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .userDetailsService(uds)
        .logout(logout -> logout
            .logoutUrl("/logout")
            .addLogoutHandler(customLogoutHandler)
            .logoutSuccessHandler((request, response, authentication) ->
                    response.setStatus(HttpServletResponse.SC_OK))
        )
      .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2UserServiceCustom()) 
                )
                
    .successHandler((request, response, authentication) -> {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String provider = token.getAuthorizedClientRegistrationId();

        Map<String, String> datos = oAuth2UserService.login(provider, oAuth2User);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(datos));
    })
    .failureHandler(failureHandler)
            );

 
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}   

@Bean
    public org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserServiceCustom() {
        return userRequest -> {
            var delegate = new DefaultOAuth2UserService();
            var oAuth2User = delegate.loadUser(userRequest);
      
            return oAuth2User;
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
