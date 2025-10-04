package com.laspalmas.api.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.laspalmas.api.model.enums.Provider;
import com.laspalmas.api.model.enums.Rol;
import com.laspalmas.api.validation.CorreoOCelular;
import com.laspalmas.api.validation.UsuarioLocal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@CorreoOCelular
@UsuarioLocal
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false) 
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 25, message = "El nombre debe tener entre 3 y 25 caracteres")
    private String nombre;

    @Column(length = 30, nullable = false) 
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 3, max = 30, message = "El apellido debe tener entre 3 y 30 caracteres")
    private String apellidos;

    
    @Column(length = 100, nullable = true) 
    private String contraseña;
  
    @Column(unique = true,length = 13, nullable = true)
    @Pattern(regexp = "\\d{10}", message = "El número telefónico debe contener exactamente 10 dígitos")
    @Size(min = 10, max = 10, message = "El numero telefonico debe tener exactamente 10 digitos")
    private String numeroCelular;

    @Column(unique = true,length = 50, nullable = true)
    @Email(message = "Debe ingresar un correo electronico válido")
    @Size(min = 10, max = 50, message = "El correo electronico debe tener entre 10 y 50 caracteres")
    private String correo;

    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Rol rol = Rol.USER; // USER o ADMIN

    private boolean isVerified = false;
     // Relación uno a uno con Provider
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private ProviderInfo providerInfo;

    // Relación uno a muchos con tokens (verificación, reset, etc.)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<TokenUsuario> tokens;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
}

