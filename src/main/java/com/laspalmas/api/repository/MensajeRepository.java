package com.laspalmas.api.repository;

import com.laspalmas.api.model.Mensaje;
import com.laspalmas.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByRemitenteAndDestinatario(Usuario remitente, Usuario destinatario);
   
}
