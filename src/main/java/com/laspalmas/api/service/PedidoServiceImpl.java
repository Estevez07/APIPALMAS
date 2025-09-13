package com.laspalmas.api.service;

import com.laspalmas.api.model.ArchivoDTO;
import com.laspalmas.api.model.PedidoDTO;
import com.laspalmas.api.model.Archivo;
import com.laspalmas.api.model.Pedido;
import com.laspalmas.api.model.Usuario;
import com.laspalmas.api.model.UsuarioDTO;
import com.laspalmas.api.repository.ArchivoRepository;
import com.laspalmas.api.repository.PedidoRepository;
import com.laspalmas.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArchivoRepository archivoRepository;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @PreAuthorize("#numeroCelular == authentication.name or hasAuthority('ADMIN')")
    public List<PedidoDTO> obtenerPedidosPorUsuario(String numeroCelular) {
        Usuario usuario = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return pedidoRepository.findByUsuario(usuario).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public PedidoDTO agregarPedido(List<MultipartFile> archivos, String numeroCelular) throws IOException {
        Usuario usuario = usuarioRepository.findByNumeroCelular(numeroCelular)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);

        List<Archivo> archivosEntidad = new ArrayList<>();

        for (MultipartFile archivo : archivos) {
            Archivo nuevoArchivo = new Archivo();
            nuevoArchivo.setNombre(archivo.getOriginalFilename());
            nuevoArchivo.setTipo(archivo.getContentType());
            nuevoArchivo.setContenido(archivo.getBytes());
            nuevoArchivo.setPedido(pedido);
            archivosEntidad.add(nuevoArchivo);
        }

        pedido.setArchivos(archivosEntidad);
        return mapToDTO(pedidoRepository.save(pedido));

    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        pedidoRepository.delete(pedido);
    }

    //  Conversión Pedido → PedidoDTO
    private PedidoDTO mapToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        
    // Crear y asignar el usuarioDTO
    Usuario usuario = pedido.getUsuario();
    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setNombre(usuario.getNombre());
    usuarioDTO.setNumeroCelular(usuario.getNumeroCelular());


    dto.setUsuario(usuarioDTO); 

        List<ArchivoDTO> archivosDTO = pedido.getArchivos().stream().map(archivo -> {
            String base64 = Base64.getEncoder().encodeToString(archivo.getContenido());
            return new ArchivoDTO(archivo.getNombre(), archivo.getTipo(), base64);
        }).toList();

        dto.setArchivos(archivosDTO);
        return dto;
    }
}
