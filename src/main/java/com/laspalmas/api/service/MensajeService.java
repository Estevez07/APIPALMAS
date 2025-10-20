package com.laspalmas.api.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.laspalmas.api.dto.MensajeDTO;

public interface MensajeService {

     MensajeDTO enviarMensaje(String Cotenido, List<MultipartFile> archivo,
                                Long idDestinatario,
                                String credencial,
                                Long idPedido) throws IOException;

     List<MensajeDTO> obtenerMensajesEntreUsuarios(String credencial, Long idDestinatario);
     MensajeDTO modificarMensaje(Long id, String nuevoContenido,List<MultipartFile> archivos, String credencial);
     void eliminarMensaje(Long id, String credencial);
}
