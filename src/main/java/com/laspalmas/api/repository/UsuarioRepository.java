package com.laspalmas.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.enums.Provider;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   
   @Query("SELECT u FROM Usuario u WHERE " +
           "(:correo IS NOT NULL AND u.correo = :correo) OR " +
           "(:numeroCelular IS NOT NULL AND u.numeroCelular = :numeroCelular)")
    Optional<Usuario> findByCorreoOrNumeroCelular(@Param("correo") String correo,
                                                  @Param("numeroCelular") String numeroCelular);
    

  @Query("SELECT u FROM Usuario u WHERE (u.correo = :credencial OR u.numeroCelular = :credencial) AND u.isVerified = true")
  Optional<Usuario> buscarPorCredencial(@Param("credencial") String credencial);

  @Query("SELECT DISTINCT u FROM Usuario u JOIN u.pedidos p")
  List<Usuario> findUsuariosConPedidos();

 Optional<Usuario> findByProviderInfoProviderAndProviderInfoProviderId(Provider provider, String providerId);

  Optional<Usuario> findByCorreo(String correo);

}