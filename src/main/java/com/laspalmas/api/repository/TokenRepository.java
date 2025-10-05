package com.laspalmas.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.TokenTipo;

import jakarta.transaction.Transactional;

public interface TokenRepository extends JpaRepository<TokenUsuario, Long> {
    List<TokenUsuario> findByUsuarioAndTipo(Usuario usuario, TokenTipo tipo);
    @Transactional
    @Modifying
    @Query("DELETE FROM TokenUsuario t WHERE t.usuario = :usuario AND t.tipo = :tipo")
    void deleteByUsuarioAndTipo(@Param("usuario") Usuario usuario, @Param("tipo") TokenTipo tipo);
    Optional<TokenUsuario> findByToken(String token);
}
