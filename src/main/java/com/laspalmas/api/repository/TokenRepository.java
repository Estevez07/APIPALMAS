package com.laspalmas.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.TokenTipo;

public interface TokenRepository extends JpaRepository<TokenUsuario, Long> {
    List<TokenUsuario> findByUsuarioAndTipo(Usuario usuario, TokenTipo tipo);
}
