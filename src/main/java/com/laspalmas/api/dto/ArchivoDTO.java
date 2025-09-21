package com.laspalmas.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchivoDTO {
    private String nombre;
    private String tipo;
    private String base64;
}
