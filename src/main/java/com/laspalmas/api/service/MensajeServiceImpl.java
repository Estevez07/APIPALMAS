package com.laspalmas.api.service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.laspalmas.api.repository.MensajeRepository;
import com.laspalmas.api.repository.PedidoRepository;
import com.laspalmas.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

import com.laspalmas.api.model.Mensaje;
import com.laspalmas.api.model.Pedido;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.dto.MensajeDTO;
import com.laspalmas.api.mapper.ArchivoMapper;
import com.laspalmas.api.mapper.MensajeMapper;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService{

    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    
    private final MensajeMapper mensajeMapper;
    private final ArchivoMapper archivoMapper;

    @Override
       public MensajeDTO enviarMensaje(String Cotenido, List<MultipartFile> archivos,
                                Long idDestinatario,
                                String numeroCelular,
                                Long idPedido) throws IOException{
       Mensaje mensaje = new Mensaje();
        mensaje.setContenido(Cotenido);
        // Set remitente y destinatario obligatorios
        Usuario remitente = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));
        Usuario destinatario = usuarioRepository.findById(idDestinatario)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));

        mensaje.setRemitente(remitente);
        mensaje.setDestinatario(destinatario);

      
    
        Optional.ofNullable(archivos)
        .filter(list -> !list.isEmpty())
        .map(list -> list.stream()
                         .map(archivoMapper::toEntity)
                         .toList())
        .ifPresent(mensaje::setArchivos);


        Optional.ofNullable(idPedido)
        .ifPresent(pid -> {
            Pedido pedido = pedidoRepository.findById(pid)
                    .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
            mensaje.setPedido(pedido);
        });


        Mensaje guardado = mensajeRepository.save(mensaje);
        return mensajeMapper.toDTO(guardado);
    }


    @Override
     public List<MensajeDTO> obtenerMensajesEntreUsuarios(String numeroCelular, Long idDestinatario) {
        Usuario remitente = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));
        Usuario destinatario = usuarioRepository.findById(idDestinatario)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));

        return mensajeRepository.findByRemitenteAndDestinatario(remitente, destinatario)
                .stream()
                .map(mensajeMapper::toDTO)
                .collect(Collectors.toList());
    }
    
}
