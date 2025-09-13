package com.laspalmas.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipo;

    @Lob
    private byte[] contenido;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
