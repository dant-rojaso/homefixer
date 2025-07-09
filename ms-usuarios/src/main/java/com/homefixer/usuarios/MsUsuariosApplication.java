package com.homefixer.usuarios; // Package base correcto

import org.springframework.boot.SpringApplication; // Clase para ejecutar aplicación Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Configuración automática de Spring Boot

@SpringBootApplication // Habilita configuración automática, escaneo de componentes y configuración
public class MsUsuariosApplication { // Nombre que coincide con tu estructura

    public static void main(String[] args) { // Método principal de la aplicación
        SpringApplication.run(MsUsuariosApplication.class, args); // Ejecuta la aplicación Spring Boot
    }
}