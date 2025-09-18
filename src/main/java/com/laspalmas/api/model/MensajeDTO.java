package com.laspalmas.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDTO {
    private Long id;
    private String contenido;
    private Date fechaEnvio;
    private String estado;

    private UsuarioDTO remitente;      
    private UsuarioDTO destinatario;    

    private List<ArchivoDTO> archivos;         // opcional
    private PedidoDTO pedido;           // opcional
 
}