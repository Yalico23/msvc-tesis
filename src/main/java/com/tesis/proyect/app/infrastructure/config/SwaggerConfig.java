package com.tesis.proyect.app.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "API Entrevistas", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI hotelCleaningMaterialsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Materiales de Limpieza - Hotel")
                        .description("API para la administración de inventario, control de stock, " +
                                "solicitudes y distribución de materiales de limpieza en un hotel. " +
                                "Incluye funcionalidades para áreas como housekeeping, bodega y compras.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Soporte Técnico - Hotel")
                                .url("https://www.hotel-ejemplo.com/soporte")
                                .email("soporte@hotel-ejemplo.com"))
                        .license(new License()
                                .name("Licencia Privada - Hotel")
                                .url("https://www.hotel-ejemplo.com/licencia")));
    }
}
