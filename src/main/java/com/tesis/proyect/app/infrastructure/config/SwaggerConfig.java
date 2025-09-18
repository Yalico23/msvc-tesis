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
    // http://localhost:8081/swagger-ui.html
    @Bean
    public OpenAPI hotelCleaningMaterialsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Entrevistas Automatizadas con IA")
                        .description("""
                                Plataforma de entrevistas automatizadas que utiliza Inteligencia Artificial
                                para optimizar los procesos de selección y evaluación de candidatos.
                                
                                Funcionalidades principales:
                                - Creación, gestión y ejecución de entrevistas automatizadas.
                                - Integración con Amazon Chime SDK para videollamadas y grabación.
                                - Generación de voz con Amazon Polly para preguntas automatizadas.
                                - Almacenamiento seguro de grabaciones y resultados en Amazon S3.
                                - Gestión de practicantes, entrevistadores y resultados evaluados por IA.
                                
                                Esta API permite a los desarrolladores integrar fácilmente servicios
                                de entrevistas inteligentes en sus sistemas de recursos humanos,
                                ofreciendo escalabilidad, trazabilidad y análisis avanzado.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Soporte Técnico - Plataforma de Entrevistas IA")
                                .url("https://www.entrevistas-ia.com/soporte")
                                .email("soporte@entrevistas-ia.com"))
                        .license(new License()
                                .name("Licencia Privada - Entrevistas IA")
                                .url("https://www.entrevistas-ia.com/licencia")))

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
