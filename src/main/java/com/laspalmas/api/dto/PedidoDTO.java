package com.laspalmas.api.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private UsuarioDTO usuario;
    private List<ArchivoDTO> archivos;
}
