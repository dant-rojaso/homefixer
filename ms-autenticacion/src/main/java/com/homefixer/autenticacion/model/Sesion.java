package com.homefixer.autenticacion.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca como entidad de base de datos
@Table(name = "sesiones") // Nombre de tabla en MySQL
@Data // Lombok: getters, setters, toString automáticos
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón builder
public class Sesion {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idSesion; // ID único de la sesión
    
    @Column(name = "id_usuario", nullable = false) // FK a ms-usuarios
    private Long idUsuario; // ID del usuario logueado
    
    @Column(name = "token_sesion", nullable = false, unique = true) // Token único de sesión
    private String tokenSesion; // Token que identifica la sesión
    
    @Column(name = "fecha_inicio") // Cuándo inició sesión
    private LocalDateTime fechaInicio; // Timestamp de login
    
    @Column(name = "fecha_ultimo_acceso") // Última actividad
    private LocalDateTime fechaUltimoAcceso; // Timestamp de última acción
    
    @Column(name = "fecha_fin") // Cuándo cerró sesión
    private LocalDateTime fechaFin; // Timestamp de logout
    
    @Enumerated(EnumType.STRING) // Estado como texto
    private EstadoSesion estado; // Estado actual de la sesión
    
    @Column(name = "ip_cliente", length = 45) // IP del cliente
    private String ipCliente; // Dirección IP desde donde se conecta
    
    @Column(name = "dispositivo", length = 100) // Tipo de dispositivo
    private String dispositivo; // Móvil, Desktop, Tablet, etc.
    
    @Column(name = "navegador", length = 100) // Navegador usado
    private String navegador; // Chrome, Firefox, Safari, etc.
    
    // Enum para estados de sesión
    public enum EstadoSesion {
        ACTIVA,     // Sesión en uso
        EXPIRADA,   // Sesión caducada por tiempo
        CERRADA,    // Sesión cerrada por usuario
        REVOCADA    // Sesión cancelada por sistema
    }
}