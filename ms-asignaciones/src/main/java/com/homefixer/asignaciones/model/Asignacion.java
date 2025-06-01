package com.homefixer.asignaciones.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok
import java.math.BigDecimal; // Para decimales precisos
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca como entidad de base de datos
@Table(name = "asignaciones") // Nombre de tabla en MySQL
@Data // Lombok: getters, setters, toString automáticos
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón builder para crear objetos
public class Asignacion {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idAsignacion; // ID único de la asignación
    
    @Column(name = "id_solicitud", nullable = false, unique = true) // FK única a ms-solicitudes
    private Long idSolicitud; // ID de la solicitud asignada
    
    @Column(name = "id_tecnico", nullable = false) // FK a ms-usuarios (técnicos)
    private Long idTecnico; // ID del técnico asignado
    
    @Column(name = "id_cliente", nullable = false) // FK a ms-usuarios (clientes)
    private Long idCliente; // ID del cliente que solicitó
    
    @Enumerated(EnumType.STRING) // Guarda enum como texto
    private EstadoAsignacion estado; // Estado actual de la asignación
    
    @Column(name = "fecha_asignacion") // Cuándo se asignó
    private LocalDateTime fechaAsignacion; // Timestamp de asignación
    
    @Column(name = "fecha_aceptacion") // Cuándo aceptó el técnico
    private LocalDateTime fechaAceptacion; // Timestamp de aceptación
    
    @Column(name = "fecha_inicio_servicio") // Cuándo empezó el trabajo
    private LocalDateTime fechaInicioServicio; // Timestamp inicio trabajo
    
    @Column(name = "fecha_fin_servicio") // Cuándo terminó el trabajo
    private LocalDateTime fechaFinServicio; // Timestamp fin trabajo
    
    @Column(name = "distancia_km", precision = 5, scale = 2) // Distancia con decimales
    private BigDecimal distanciaKm; // Distancia entre técnico y cliente
    
    @Column(name = "tiempo_estimado_minutos") // Tiempo estimado de llegada
    private Integer tiempoEstimadoMinutos; // ETA en minutos
    
    @Column(name = "costo_final", precision = 10, scale = 2) // Costo final del servicio
    private BigDecimal costoFinal; // Precio total cobrado
    
    @Column(name = "observaciones_tecnico", length = 500) // Notas del técnico
    private String observacionesTecnico; // Comentarios del técnico sobre el trabajo
    
    @Column(name = "motivo_rechazo", length = 300) // Por qué rechazó
    private String motivoRechazo; // Razón si rechaza la asignación
    
    // Enum para los estados de una asignación
    public enum EstadoAsignacion {
        PROPUESTA,    // Enviada al técnico, esperando respuesta
        ACEPTADA,     // Técnico aceptó la asignación
        RECHAZADA,    // Técnico rechazó la asignación
        EN_CAMINO,    // Técnico va hacia el cliente
        EN_SERVICIO,  // Técnico está trabajando
        COMPLETADA,   // Trabajo terminado exitosamente
        CANCELADA     // Cancelada por alguna razón
    }
}