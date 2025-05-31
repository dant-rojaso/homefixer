package com.homefixer.usuarios;

import org.springframework.boot.SpringApplication; // Para ejecutar aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Configuración automática
import lombok.extern.slf4j.Slf4j; // Logger automático

@SpringBootApplication // Habilita auto-configuración, escaneo de componentes y configuración
@Slf4j // Logger automático
public class UsuariosApplication {
    
    // Método principal que inicia la aplicación
    public static void main(String[] args) {
        log.info("🚀 Iniciando microservicio ms-usuarios..."); // Log de inicio
        
        SpringApplication.run(UsuariosApplication.class, args); // Ejecuta la aplicación Spring Boot
        
        log.info("✅ Microservicio ms-usuarios iniciado correctamente en puerto 8080"); // Log de éxito
    }
}