package com.laspalmas.api.controller;

import com.laspalmas.api.dto.PedidoDTO;
import com.laspalmas.api.service.PedidoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

 
    private final PedidoService pedidoService;

    //  Solo ADMIN
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }

    //  ADMIN o el mismo usuario autenticado
    @GetMapping("/{numeroCelular}")
    public ResponseEntity<List<PedidoDTO>> obtenerPorUsuario(@PathVariable String numeroCelular) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(numeroCelular));
    }

    //  USER autenticado
    @PostMapping
    public ResponseEntity<PedidoDTO> agregarPedido(@RequestParam("archivos") List<MultipartFile> archivos,
                                                   @AuthenticationPrincipal User usuarioAutenticado) throws IOException {
        String numeroCelular = usuarioAutenticado.getUsername();
        return ResponseEntity.ok(pedidoService.agregarPedido(archivos, numeroCelular));
    }

    //  Solo ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.ok("Pedido eliminado");
    }
}
