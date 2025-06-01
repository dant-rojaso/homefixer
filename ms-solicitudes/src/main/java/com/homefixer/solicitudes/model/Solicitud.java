package com.homefixer.solicitudes.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok
import java.math.BigDecimal; // Para decimales con precisión
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca como entidad de base de datos
@Table(name = "solicitudes") // Nombre de tabla en MySQL
@Data // Lombok: getters, setters, toString automáticos
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón builder para crear objetos
public class Solicitud {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idSolicitud; // ID único de la solicitud
    
    @Column(name = "id_cliente", nullable = false) // FK obligatoria a ms-usuarios
    private Long idCliente; // ID del cliente que solicita el servicio
    
    @Column(name = "titulo", nullable = false, length = 200) // Título obligatorio
    private String titulo; // Título del servicio solicitado
    
    @Column(name = "descripcion", nullable = false, length = 1000) // Descripción obligatoria
    private String descripcion; // Descripción detallada del problema
    
    @Column(name = "especialidad_requerida", nullable = false, length = 100) // Especialidad obligatoria
    private String especialidadRequerida; // Tipo de técnico necesario (Plomería, Electricidad, etc.)
    
    @Column(name = "direccion_servicio", nullable = false, length = 200) // Dirección obligatoria
    private String direccionServicio; // Donde se realizará el servicio
    
    @Column(name = "ciudad", length = 50) // Ciudad opcional
    private String ciudad; // Ciudad del servicio
    
    @Column(name = "region", length = 50) // Región opcional
    private String region; // Región del servicio
    
    @Enumerated(EnumType.STRING) // Guarda enum como texto
    private EstadoSolicitud estado; // Estado actual de la solicitud
    
    @Enumerated(EnumType.STRING) // Prioridad como texto
    private PrioridadSolicitud prioridad; // Urgencia del servicio
    
    @Column(name = "presupuesto_estimado", precision = 10, scale = 2) // Dinero con decimales
    private BigDecimal presupuestoEstimado; // Cuánto está dispuesto a pagar el cliente
    
    @Column(name = "fecha_creacion") // Cuándo se creó
    private LocalDateTime fechaCreacion; // Timestamp de creación
    
    @Column(name = "fecha_preferida") // Cuándo prefiere el servicio
    private LocalDateTime fechaPreferida; // Fecha/hora preferida por el cliente
    
    @Column(name = "observaciones", length = 500) // Observaciones opcionales
    private String observaciones; // Notas adicionales del cliente
    
    // Enum para los estados posibles de una solicitud
    public enum EstadoSolicitud {
        PENDIENTE,   // Recién creada, esperando técnicos
        ASIGNADA,    // Ya tiene técnico asignado
        EN_PROCESO,  // El técnico está trabajando
        COMPLETADA,  // Servicio terminado
        CANCELADA    // Cancelada por cliente o técnico
    }
    
    // Enum para la prioridad de la solicitud
    public enum PrioridadSolicitud {
        BAJA,      // No urgente
        MEDIA,     // Normal
        ALTA,      // Urgente
        CRITICA    // Emergencia
    }
}