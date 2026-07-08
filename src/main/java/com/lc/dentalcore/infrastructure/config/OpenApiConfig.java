package com.lc.dentalcore.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dentalCoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dental Core API")
                        .description("API REST para la gestión de un consultorio odontológico.")
                        .version("1.0.0"));
    }
}
