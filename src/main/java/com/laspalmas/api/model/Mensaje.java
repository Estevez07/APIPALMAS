package com.laspalmas.api.model;

import java.util.Date;
import java.util.List;

import com.laspalmas.api.model.enu.EstadoMensaje;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "remitente_id")
    private Usuario remitente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    @Column(length = 500)
    private String contenido; // Texto libre (puede ser null si es solo pedido o archivo)

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio = new Date();

    @Enumerated(EnumType.STRING)
    private EstadoMensaje estado = EstadoMensaje.ENVIADO;

    // Opcional: mensaje con archivo adjunto
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "archivo_id")
    List<Archivo> archivos;

    // Opcional: mensaje que representa un pedido
    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;
}
