package com.laspalmas.api.controller;

import com.laspalmas.api.dto.MensajeDTO;
import com.laspalmas.api.service.MensajeService;
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
@RequestMapping("/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;

    // Endpoint para enviar un mensaje
    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO> enviarMensaje(
            @RequestParam String contenido,
            @RequestParam(required = false) List<MultipartFile> archivos,
            @RequestParam Long idDestinatario,
            @AuthenticationPrincipal User usuarioAutenticado,
            @RequestParam(required = false) Long idPedido
    ) throws IOException {


         String credencial = usuarioAutenticado.getUsername();

 return ResponseEntity.status(HttpStatus.CREATED).body(mensajeService.enviarMensaje(contenido,archivos,idDestinatario,credencial,idPedido));


    }

    // Endpoint para obtener todos los mensajes entre dos usuarios
    @GetMapping("/entre")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajesEntreUsuarios( 
            @AuthenticationPrincipal User usuarioAutenticado,
            @RequestParam Long idDestinatario
    ) {
        
        String credencial = usuarioAutenticado.getUsername();

       return ResponseEntity.ok(mensajeService.obtenerMensajesEntreUsuarios(credencial,idDestinatario));
    }


// Modificar mensaje (agregar un mapper si es que vienen archivos nuevos........---------------------)
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarMensaje(
            @PathVariable Long id,
            @RequestParam String nuevoContenido,
            @AuthenticationPrincipal User usuarioAutenticado
    ) {
       
            String credencial = usuarioAutenticado.getUsername();
            MensajeDTO actualizado = mensajeService.modificarMensaje(id, nuevoContenido, credencial);
            return ResponseEntity.ok(actualizado); // 200 OK
        
    }


     @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMensaje(
            @PathVariable Long id,
            @AuthenticationPrincipal User usuarioAutenticado
    ) {
        
            String credencial = usuarioAutenticado.getUsername();
            mensajeService.eliminarMensaje(id, credencial);
            return ResponseEntity.noContent().build();
       
    }


}
