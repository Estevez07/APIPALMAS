package com.laspalmas.api.service;
import java.util.Map;

import com.laspalmas.api.model.Usuario;

public interface AuthService {
    String registrar(Usuario usuario);
    Map<String, String>login(String credencial, String contraseña);
    String recuperarPassword(String correo);
}