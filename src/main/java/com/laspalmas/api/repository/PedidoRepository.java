package com.laspalmas.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laspalmas.api.model.Pedido;
import com.laspalmas.api.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
      List<Pedido> findByUsuario(Usuario usuario);
}