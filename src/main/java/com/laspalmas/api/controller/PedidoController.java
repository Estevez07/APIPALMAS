package com.laspalmas.api.controller;

import com.laspalmas.api.constant.ApiPaths;
import com.laspalmas.api.dto.PedidoDTO;
import com.laspalmas.api.service.PedidoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PEDIDOS)
public class PedidoController {

 
    private final PedidoService pedidoService;

    //  Solo ADMIN
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosLosPedidos());
    }

    //  ADMIN o el mismo usuario autenticado
    @GetMapping(ApiPaths.PEDIDOS_CREDENCIAL)
    public ResponseEntity<List<PedidoDTO>> obtenerPorUsuario(@PathVariable String credencial) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(credencial));
    }

    //  USER autenticado
    @PostMapping
    public ResponseEntity<PedidoDTO> agregarPedido(@RequestParam("archivos") List<MultipartFile> archivos,
                                                   @AuthenticationPrincipal User usuarioAutenticado) throws IOException {
        String credencial = usuarioAutenticado.getUsername();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.agregarPedido(archivos, credencial));
    }

    //  Solo ADMIN
    @DeleteMapping(ApiPaths.PEDIDOS_ID)
    public ResponseEntity<String> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping(ApiPaths.PEDIDOS_ID)
    public ResponseEntity<PedidoDTO> modificarPedido(
            @PathVariable Long idPedido,
            @RequestParam(required = false) List<MultipartFile> archivos,
            @AuthenticationPrincipal User usuarioAutenticado
    ) throws IOException {
       
            String credencial = usuarioAutenticado.getUsername();
            PedidoDTO actualizado = pedidoService.modificarPedido(idPedido, archivos, credencial);
            return ResponseEntity.ok(actualizado); // 200 OK
      
   
}
}