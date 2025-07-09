package com.homefixer.autenticacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "autenticaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autenticacion extends RepresentationModel<Autenticacion> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutenticacion;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
    
    @NotNull(message = "El tipo de usuario es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario; // FK al microservicio usuarios
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    private LocalDateTime ultimoLogin;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoSesion estadoSesion;
    
    private String tokenSesion;
    
    private LocalDateTime fechaExpiracion;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        estadoSesion = EstadoSesion.INACTIVA;
    }
}
