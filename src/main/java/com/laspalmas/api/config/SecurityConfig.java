package com.laspalmas.api.config;


import com.laspalmas.api.security.JwtFilter;
import com.laspalmas.api.service.impl.UsuarioDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) 
public class SecurityConfig {

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
            .requestMatchers("/auth/**", "/oauth2/**", "/login/**").permitAll()
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
            .defaultSuccessUrl("/login/success", true)
            .failureUrl("/login/failed")
        );

 
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
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
