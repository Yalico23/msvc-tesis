package com.tesis.proyect.app.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI hotelCleaningMaterialsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Entrevistas")
                        .description("API para la administración de inventario, control de stock, " +
                                "solicitudes y distribución de materiales de limpieza en un hotel.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Soporte Técnico - Hotel")
                                .url("https://www.hotel-ejemplo.com/soporte")
                                .email("soporte@hotel-ejemplo.com"))
                        .license(new License()
                                .name("Licencia Privada - Hotel")
                                .url("https://www.hotel-ejemplo.com/licencia")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticación")));
    }
}
