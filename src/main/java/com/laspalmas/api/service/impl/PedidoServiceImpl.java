package com.laspalmas.api.service.impl;

import com.laspalmas.api.model.Archivo;
import com.laspalmas.api.model.Pedido;
import com.laspalmas.api.model.Usuario;


import com.laspalmas.api.repository.PedidoRepository;
import com.laspalmas.api.repository.UsuarioRepository;
import com.laspalmas.api.service.PedidoService;
import com.laspalmas.api.dto.PedidoDTO;
import com.laspalmas.api.mapper.ArchivoMapper;
import com.laspalmas.api.mapper.PedidoMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;

    private final PedidoMapper pedidoMapper;

    private final ArchivoMapper archivoMapper;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDTO) 
                .toList();
    }

    @Override
    @PreAuthorize("#credencial == authentication.name or hasAuthority('ADMIN')")
    public List<PedidoDTO> obtenerPedidosPorUsuario(String credencial) {
        Usuario usuario = usuarioRepository.buscarPorCredencial(credencial)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

     return pedidoRepository.findByUsuario(usuario).stream()
        .map(pedidoMapper::toDTO)   
        .toList();
      
    }

    @Override
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public PedidoDTO agregarPedido(List<MultipartFile> archivos, String credencial) throws IOException {
        Usuario usuario = usuarioRepository.buscarPorCredencial(credencial)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);

   List<Archivo> archivosEntidad = archivos.stream()
    .map(archivoMapper::toEntity)
    .peek(a -> a.setPedido(pedido)) // se setea el pedido
    .toList();

        pedido.setArchivos(archivosEntidad);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoGuardado);

    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        pedidoRepository.delete(pedido);
    }

@Override
@PreAuthorize("#credencial == authentication.name or hasAuthority('ADMIN')")
public PedidoDTO modificarPedido(Long idPedido,
                                  List<MultipartFile> archivos,
                                  String credencial) throws IOException {
    Pedido pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

      // Si vienen archivos -> reemplazar la lista
    if (archivos != null && !archivos.isEmpty()) {
        List<Archivo> archivosEntidad = archivos.stream()
                .map(archivoMapper::toEntity)
                .peek(a -> a.setPedido(pedido))
                .toList();
        pedido.setArchivos(archivosEntidad);
    }

    Pedido actualizado = pedidoRepository.save(pedido);
    return pedidoMapper.toDTO(actualizado);
}

}
