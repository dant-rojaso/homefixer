package com.homefixer.solicitudes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa una solicitud de servicio técnico
 */
@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Solicitud extends RepresentationModel<Solicitud> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;
    
    @NotNull(message = "El cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;
    
    @Column(nullable = true)
    private Long idTecnico;
    
    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Column(nullable = false)
    private String tipoServicio;
    
    @NotBlank(message = "La descripción del problema es obligatoria")
    @Column(nullable = false, length = 500)
    private String descripcionProblema;
    
    @NotBlank(message = "La dirección del servicio es obligatoria")
    @Column(nullable = false)
    private String direccionServicio;
    
    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;
    
    @NotNull(message = "La fecha de servicio es obligatoria")
    @Future(message = "La fecha de servicio debe ser futura")
    @Column(nullable = false)
    private LocalDate fechaServicio;
    
    @NotNull(message = "El estado de la solicitud es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoSolicitud;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El presupuesto debe ser mayor a 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal presupuestoEstimado;
    
    @Column(length = 500)
    private String observaciones;
    
    /**
     * Método para establecer valores por defecto antes de persistir
     */
    @PrePersist
    protected void onCreate() {
        fechaSolicitud = LocalDateTime.now();
        if (estadoSolicitud == null) {
            estadoSolicitud = EstadoSolicitud.PENDIENTE;
        }
    }
}