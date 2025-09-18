package com.laspalmas.api.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.laspalmas.api.model.MensajeDTO;

public interface MensajeService {

     MensajeDTO enviarMensaje(String Cotenido, List<MultipartFile> archivo,
                                Long idDestinatario,
                                String numeroCelular,
                                Long idPedido) throws IOException;

     List<MensajeDTO> obtenerMensajesEntreUsuarios(String numeroCelular, Long idDestinatario);
}
