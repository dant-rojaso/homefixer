package com.homefixer.maestrias.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "insignias_maestrias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsigniaMaestria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsignia;
    
    @NotNull
    @Column(nullable = false)
    private Long idTecnico; // FK al microservicio usuarios
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaServicio categoria;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelMaestria nivelMaestria;
    
    @Column(nullable = false)
    private LocalDateTime fechaObtencion;
    
    @Column(nullable = false)
    private Boolean activa;
    
    @PrePersist
    protected void onCreate() {
        fechaObtencion = LocalDateTime.now();
        if (activa == null) {
            activa = true;
        }
    }
}
