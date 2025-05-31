package com.homefixer.usuarios.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok para getters/setters automáticos
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca esta clase como entidad de base de datos
@Table(name = "usuarios") // Nombre de la tabla en MySQL
@Data // Lombok: genera getters, setters, toString automáticamente
@NoArgsConstructor // Lombok: constructor sin parámetros
@AllArgsConstructor // Lombok: constructor con todos los parámetros  
@Builder // Lombok: patrón builder para crear objetos
public class Usuario {
    
    @Id // Marca este campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento en MySQL
    private Long idUsuario; // ID único del usuario
    
    @Column(name = "nombre", nullable = false, length = 100) // Columna obligatoria, máximo 100 caracteres
    private String nombre; // Nombre completo del usuario
    
    @Column(name = "email", nullable = false, unique = true, length = 150) // Email único en toda la tabla
    private String email; // Correo electrónico para login
    
    @Column(name = "telefono", length = 20) // Teléfono opcional
    private String telefono; // Número de contacto
    
    @Column(name = "password", nullable = false) // Contraseña obligatoria
    private String password; // Contraseña encriptada (en proyecto real usar BCrypt)
    
    @Enumerated(EnumType.STRING) // Guarda el enum como texto en BD
    private TipoUsuario tipo; // CLIENTE o TECNICO
    
    @Column(name = "activo") // Estado del usuario
    private Boolean activo; // true = activo, false = deshabilitado
    
    @Column(name = "fecha_registro") // Cuándo se registró
    private LocalDateTime fechaRegistro; // Timestamp del registro
    
    // Enum para definir los tipos de usuario posibles
    public enum TipoUsuario {
        CLIENTE, // Usuario que solicita servicios
        TECNICO  // Usuario que presta servicios
    }
}