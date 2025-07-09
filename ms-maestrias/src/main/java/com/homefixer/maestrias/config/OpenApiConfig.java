package com.homefixer.maestrias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI maestriasOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("HomeFixer - Microservicio Maestrías")
                .description("API para gestión de maestrías, certificaciones y reputación de técnicos")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipo HomeFixer")
                    .email("dev@homefixer.com")));
    }
}
