package com.homefixer.autenticacion;

import org.springframework.boot.SpringApplication; // Importa SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication; // Importa configuración

@SpringBootApplication // Configuración automática Spring Boot
public class AutenticacionApplication {
    
    // Método principal
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando microservicio ms-autenticacion..."); // Log inicio
        
        SpringApplication.run(AutenticacionApplication.class, args); // Ejecuta aplicación
        
        System.out.println("✅ Microservicio ms-autenticacion iniciado en puerto 8081"); // Log éxito
    }
}