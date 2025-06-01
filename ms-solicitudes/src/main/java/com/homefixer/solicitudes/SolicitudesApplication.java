package com.homefixer.solicitudes;

import org.springframework.boot.SpringApplication; // Importa SpringApplication CORRECTO
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importa configuración automática

@SpringBootApplication // Configuración automática de Spring Boot
public class SolicitudesApplication {
    
    // Método principal - punto de entrada de la aplicación
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando microservicio ms-solicitudes..."); // Log simple con System.out
        
        SpringApplication.run(SolicitudesApplication.class, args); // Ejecuta la aplicación
        
        System.out.println("✅ Microservicio ms-solicitudes iniciado en puerto 8082"); // Log de éxito
    }
}