package com.laspalmas.api.service;
import java.util.Map;

import com.laspalmas.api.model.Usuario;

public interface AuthService {
    String registrar(Usuario usuario);
    public Map<String, String>login(String numeroCelular, String contraseña);
}