package com.laspalmas.api.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.laspalmas.api.model.MensajeDTO;

public interface MensajeService {

     MensajeDTO enviarMensaje(String Cotenido, List<MultipartFile> archivo,
                                Long idDestinatario,
                                Long idRemitente,
                                Long idPedido);

     List<MensajeDTO> obtenerMensajesEntreUsuarios(Long remitenteId, Long destinatarioId);
}
