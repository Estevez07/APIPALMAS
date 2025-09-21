package com.laspalmas.api.service;

import org.springframework.web.multipart.MultipartFile;

import com.laspalmas.api.dto.PedidoDTO;

import java.io.IOException;
import java.util.List;

public interface PedidoService {

    List<PedidoDTO> obtenerTodosLosPedidos();

    List<PedidoDTO> obtenerPedidosPorUsuario(String numeroCelular);

    // Acepta lista de archivos y devuelve Pedido con metadatos
    PedidoDTO agregarPedido(List<MultipartFile> archivos, String numeroCelular) throws IOException;

    void eliminarPedido(Long id);

    PedidoDTO modificarPedido(Long id,
                                  List<MultipartFile> archivos,
                                  String numeroCelular) throws IOException ;
}
