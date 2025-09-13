package com.laspalmas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.laspalmas.api.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNumeroCelular(String numeroCelular);
    
     @Query("SELECT DISTINCT u FROM Usuario u JOIN u.pedidos p")
    List<Usuario> findUsuariosConPedidos();
}