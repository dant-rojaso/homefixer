package com.homefixer.usuarios.model;

import jakarta.persistence.*; // Anotaciones JPA
import jakarta.validation.constraints.*; // Validaciones
import lombok.*; // Lombok para reducir código

@Entity // Marca como entidad JPA
@Table(name = "tecnicos") // Nombre de tabla en BD
@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los parámetros
public class Tecnico {
    
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    private Long idTecnico; // ID único del técnico
    
    @NotBlank(message = "El nombre es obligatorio") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String nombre; // Nombre del técnico
    
    @NotBlank(message = "El apellido es obligatorio") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String apellido; // Apellido del técnico
    
    @Email(message = "Email debe ser válido") // Validación formato email
    @NotBlank(message = "El email es obligatorio") // Validación no vacío
    @Column(nullable = false, unique = true) // Campo único en BD
    private String email; // Email único del técnico
    
    @Pattern(regexp = "^[0-9]{9}$", message = "Teléfono debe tener 9 dígitos") // Validación formato
    @Column(nullable = false) // Campo obligatorio en BD
    private String telefono; // Teléfono del técnico
    
    @NotBlank(message = "La especialidad es obligatoria") // Validación no vacío
    @Column(nullable = false) // Campo obligatorio en BD
    private String especialidad; // Especialidad del técnico
    
    @Column(nullable = false) // Campo obligatorio en BD
    private Boolean disponible = true; // Estado de disponibilidad del técnico
}