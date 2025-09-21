package com.laspalmas.api.model;

import java.util.Date;
import java.util.List;

import com.laspalmas.api.model.enums.Provider;
import com.laspalmas.api.model.enums.Rol;
import com.laspalmas.api.validation.CorreoOCelular;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@CorreoOCelular
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

    @Column(length = 100, nullable = false) 
    @NotBlank(message = "La contraseña no puede estar vacío")
    @Size(min = 8, message = "La contraseña debe tener mas de 8 caracteres")
    private String contraseña;
  
    @Column(unique = true,length = 13, nullable = false)
    @NotBlank(message = "El numero telefonico no puede estar vacío")
    @Pattern(regexp = "\\d{13}", message = "El número telefónico debe contener exactamente 13 dígitos")
    @Size(min = 13, max = 13, message = "El numero telefonico debe tener exactamente 13 digitos")
    private String numeroCelular;

    @Column(unique = true,length = 50, nullable = false)
    @NotBlank(message = "El correo electronico no puede estar vacío")
    @Email(message = "Debe ingresar un correo electronico válido")
    @Size(min = 10, max = 50, message = "El correo electronico debe tener entre 10 y 50 caracteres")
    private String correo;

    @NotNull(message = "La fecha de nacimiento no debe estar vacía")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Rol rol; // USER o ADMIN

      // De dónde proviene el usuario
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Provider provider = Provider.LOCAL;

    // ID que devuelve el proveedor (ej. el id de Facebook)
    @Column(length = 100, unique = true)
    private String providerId;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
}

