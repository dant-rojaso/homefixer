package com.homefixer.solicitudes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para OpenAPI/Swagger
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "HomeFixer - Microservicio de Solicitudes",
        version = "1.0.0",
        description = "API REST para gestión de solicitudes de servicios técnicos domiciliarios",
        contact = @Contact(
            name = "Equipo HomeFixer",
            email = "support@homefixer.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Servidor de Desarrollo",
            url = "http://localhost:8082"
        )
    }
)
public class OpenApiConfig {
}