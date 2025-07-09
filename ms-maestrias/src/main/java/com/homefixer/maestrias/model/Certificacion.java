package com.homefixer.maestrias.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCertificacion;
    
    @NotNull
    @Column(nullable = false)
    private Long idTecnico; // FK al microservicio usuarios
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaServicio categoria;
    
    @NotBlank
    @Column(nullable = false)
    private String tipoCertificacion;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCertificacion estado;
    
    @Column(nullable = false)
    private LocalDateTime fechaSubida;
    
    private LocalDateTime fechaValidacion;
    
    @Column(length = 500)
    private String observaciones;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoCertificacion.PENDIENTE;
        }
    }
}