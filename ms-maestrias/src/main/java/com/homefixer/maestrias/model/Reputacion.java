package com.homefixer.maestrias.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reputaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reputacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReputacion;
    
    @NotNull
    @Column(nullable = false)
    private Long idTecnico; // FK al microservicio usuarios
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaServicio categoria;
    
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal nivelReputacion;
    
    @NotNull
    @Column(nullable = false)
    private Integer valoracionesRecibidas;
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
