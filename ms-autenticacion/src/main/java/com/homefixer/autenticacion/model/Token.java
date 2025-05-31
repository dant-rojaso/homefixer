package com.homefixer.autenticacion.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca como entidad de base de datos
@Table(name = "tokens") // Nombre de tabla en MySQL
@Data // Lombok: getters, setters, toString automáticos
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón builder para crear objetos
public class Token {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idToken; // ID único del token
    
    @Column(name = "id_usuario", nullable = false) // FK a ms-usuarios
    private Long idUsuario; // ID del usuario propietario del token
    
    @Column(name = "token", nullable = false, unique = true, length = 500) // Token único
    private String token; // Token JWT o string generado
    
    @Enumerated(EnumType.STRING) // Guarda enum como texto
    private TipoToken tipo; // Tipo de token (LOGIN, REFRESH, RESET_PASSWORD)
    
    @Column(name = "fecha_creacion") // Cuándo se creó el token
    private LocalDateTime fechaCreacion; // Timestamp de creación
    
    @Column(name = "fecha_expiracion") // Cuándo expira
    private LocalDateTime fechaExpiracion; // Timestamp de expiración
    
    @Column(name = "activo") // Si el token está activo
    private Boolean activo; // true = válido, false = revocado
    
    @Column(name = "ip_origen", length = 45) // IP desde donde se generó
    private String ipOrigen; // Dirección IP del cliente
    
    @Column(name = "user_agent", length = 500) // Navegador/app que lo generó
    private String userAgent; // Información del cliente
    
    // Enum para los tipos de token
    public enum TipoToken {
        LOGIN,          // Token de sesión normal
        REFRESH,        // Token para renovar sesión
        RESET_PASSWORD  // Token para resetear contraseña
    }
}