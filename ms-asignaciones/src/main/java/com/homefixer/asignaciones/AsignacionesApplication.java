package com.homefixer.asignaciones;

import org.springframework.boot.SpringApplication; // Importa SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importa configuración

@SpringBootApplication // Configuración automática Spring Boot
public class AsignacionesApplication {
    
    // Método principal
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando microservicio ms-asignaciones..."); // Log inicio
        
        SpringApplication.run(AsignacionesApplication.class, args); // Ejecuta aplicación
        
        System.out.println("✅ Microservicio ms-asignaciones iniciado en puerto 8083"); // Log éxito
    }
}