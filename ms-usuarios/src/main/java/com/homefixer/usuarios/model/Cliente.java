package com.homefixer.usuarios.model;

import jakarta.persistence.*; // Importa anotaciones JPA
import lombok.*; // Importa Lombok para código automático
import java.time.LocalDateTime; // Para fechas con hora

@Entity // Marca como entidad de base de datos
@Table(name = "clientes") // Tabla separada para clientes
@Data // Lombok: getters, setters, toString automáticos
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
@Builder // Patrón builder para crear objetos
public class Cliente {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento
    private Long idCliente; // ID único del cliente
    
    @Column(name = "id_usuario", nullable = false, unique = true) // Relación con usuario
    private Long idUsuario; // Foreign key a tabla usuarios
    
    @Column(name = "direccion", length = 200) // Dirección opcional
    private String direccion; // Dirección principal del cliente
    
    @Column(name = "ciudad", length = 50) // Ciudad opcional
    private String ciudad; // Ciudad donde vive
    
    @Column(name = "region", length = 50) // Región opcional
    private String region; // Región del país
    
    @Column(name = "fecha_nacimiento") // Fecha de nacimiento opcional
    private LocalDateTime fechaNacimiento; // Para calcular edad
    
    @Enumerated(EnumType.STRING) // Guarda enum como texto
    private TipoCliente tipoCliente; // REGULAR o PREMIUM
    
    @Column(name = "servicios_contratados") // Contador de servicios
    private Integer serviciosContratados; // Total de servicios que ha pedido
    
    // Enum para clasificar tipos de cliente
    public enum TipoCliente {
        REGULAR, // Cliente normal
        PREMIUM  // Cliente con beneficios especiales
    }
}