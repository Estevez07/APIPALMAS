package com.laspalmas.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100, nullable = false)
    @NotBlank(message = "El nombre del archivo no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;


    @Column(length = 50, nullable = false)
    @NotBlank(message = "El tipo de archivo no puede estar vacío")
    @Pattern(regexp = "^(pdf|jpg|jpeg|png|docx?)$", 
             message = "El tipo de archivo debe ser pdf, jpg, jpeg, png o doc/docx")
    private String tipo;


    @Lob
    @Basic(fetch = FetchType.LAZY) // Mejor para performance
    @NotNull(message = "El contenido no puede ser nulo")
    @Size(max = 10485760, message = "El archivo no puede superar los 10MB") // 10MB aprox
    private byte[] contenido;

    @ManyToOne(optional = true)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
