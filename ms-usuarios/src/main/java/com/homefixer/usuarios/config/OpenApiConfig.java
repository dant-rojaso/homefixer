package com.homefixer.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI; // Configuración OpenAPI
import io.swagger.v3.oas.models.info.Info; // Información de la API
import io.swagger.v3.oas.models.info.Contact; // Contacto del desarrollador
import org.springframework.context.annotation.Bean; // Bean de Spring
import org.springframework.context.annotation.Configuration; // Marca como configuración

@Configuration // Marca como clase de configuración de Spring
public class OpenApiConfig {
    
    @Bean // Crea bean de configuración OpenAPI
    public OpenAPI customOpenAPI() {
        return new OpenAPI() // Configuración personalizada de OpenAPI
                .info(new Info() // Información de la API
                        .title("HomeFixer - API de Usuarios") // Título en Swagger
                        .description("Microservicio para gestión de clientes y técnicos de HomeFixer") // Descripción
                        .version("1.0.0") // Versión de la API
                        .contact(new Contact() // Información de contacto
                                .name("Equipo HomeFixer") // Nombre del equipo
                                .email("contacto@homefixer.com"))); // Email de contacto
    }
}