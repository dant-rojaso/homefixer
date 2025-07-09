package com.homefixer.autenticacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI homefixerAutenticacionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HomeFixer - Microservicio Autenticación")
                        .description("API para gestión de autenticación de usuarios en HomeFixer")
                        .version("1.0.0"));
    }
}
