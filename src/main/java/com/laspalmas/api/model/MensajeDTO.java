package com.laspalmas.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDTO {
    private Long id;
    private String contenido;
    private Date fechaEnvio;
    private String estado;

    private Long remitenteId;
    private String remitenteNombre;

    private Long destinatarioId;
    private String destinatarioNombre;

    private Long archivoId; // opcional
    private Long pedidoId;  // opcional
}