package com.homefixer.usuarios.model;

import jakarta.persistence.*; // Anotaciones JPA
import jakarta.validation.constraints.*; // Validaciones
import lombok.*; // Lombok para reducir código

@Entity // Marca como entidad JPA
@Table(name = "clientes") // Nombre de tabla en BD
@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
public class Cliente {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    private Long idCliente; // ID único del cliente
    
    @NotBlank(message = "El nombre es obligatorio") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String nombre; // Nombre del cliente
    
    @NotBlank(message = "El apellido es obligatorio") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String apellido; // Apellido del cliente
    
    @Email(message = "Email debe ser válido") // Validación formato email
    @NotBlank(message = "El email es obligatorio") // Validación no vacío
    @Column(nullable = false, unique = true) // Campo único en BD
    private String email; // Email único del cliente
    
    @Pattern(regexp = "^[0-9]{9}$", message = "Teléfono debe tener 9 dígitos") // Validación formato
    @Column(nullable = false) // Campo obligatorio en BD
    private String telefono; // Teléfono del cliente
    
    @NotBlank(message = "La dirección es obligatoria") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String direccion; // Dirección del cliente
}