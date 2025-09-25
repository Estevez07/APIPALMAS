package com.laspalmas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Provider;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   
  Optional<Usuario> findByCorreoOrNumeroCelular(String correo, String numeroCelular);
    

  @Query("SELECT u FROM Usuario u WHERE u.correo = :credencial OR u.numeroCelular = :credencial")
  Optional<Usuario> buscarPorCredencial(@Param("credencial") String credencial);

  @Query("SELECT DISTINCT u FROM Usuario u JOIN u.pedidos p")
  List<Usuario> findUsuariosConPedidos();

  Optional<Usuario> findByProviderAndProviderId(Provider provider, String providerId);
}