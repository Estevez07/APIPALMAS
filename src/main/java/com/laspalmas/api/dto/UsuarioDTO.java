package com.laspalmas.api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String nombre;
    private String apellidos;
    private String numeroCelular;
    private String correo;
    private Date fechaNac;
}
