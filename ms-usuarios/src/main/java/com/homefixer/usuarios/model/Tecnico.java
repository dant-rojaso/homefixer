package com.homefixer.usuarios.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok
import java.math.BigDecimal; // Para manejar decimales con precisión
import java.time.LocalDateTime; // Para fechas

@Entity // Entidad de base de datos
@Table(name = "tecnicos") // Tabla separada para técnicos
@Data // Lombok: métodos automáticos
@NoArgsConstructor // Constructor sin parámetros
@AllArgsConstructor // Constructor completo
@Builder // Patrón builder
public class Tecnico {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idTecnico; // ID único del técnico
    
    @Column(name = "id_usuario", nullable = false, unique = true) // FK a usuarios
    private Long idUsuario; // Relación con tabla usuarios
    
    @Column(name = "especialidad", nullable = false, length = 100) // Especialidad obligatoria
    private String especialidad; // Ej: "Plomería", "Electricidad"
    
    @Column(name = "experiencia_anos") // Años de experiencia
    private Integer experienciaAnos; // Cuántos años tiene de experiencia
    
    @Column(name = "tarifa_hora", precision = 10, scale = 2) // Precio por hora con decimales
    private BigDecimal tarifaHora; // Cuánto cobra por hora de trabajo
    
    @Column(name = "calificacion_promedio", precision = 3, scale = 2) // Calificación de 0.00 a 5.00
    private BigDecimal calificacionPromedio; // Promedio de todas sus calificaciones
    
    @Column(name = "servicios_completados") // Contador de trabajos
    private Integer serviciosCompletados; // Total de servicios que ha terminado
    
    @Enumerated(EnumType.STRING) // Enum como texto
    private EstadoTecnico estado; // DISPONIBLE, OCUPADO, INACTIVO
    
    @Column(name = "descripcion", length = 500) // Descripción opcional
    private String descripcion; // Descripción de sus servicios
    
    @Column(name = "fecha_ultimo_servicio") // Última vez que trabajó
    private LocalDateTime fechaUltimoServicio; // Para saber actividad reciente
    
    // Enum para el estado actual del técnico
    public enum EstadoTecnico {
        DISPONIBLE, // Puede recibir nuevos trabajos
        OCUPADO,    // Está trabajando actualmente
        INACTIVO    // No está disponible temporalmente
    }
}