package com.uzair.bfhl.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class to customize OpenAPI definition and Swagger UI documentation details.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bfhlOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bajaj Finserv Health Limited (BFHL) REST API")
                        .description("Production-ready, highly robust REST API built with Spring Boot 3, Java 21, and Maven. " +
                                     "Provides automated grouping, categorization, summing, and custom text transformations " +
                                     "for dynamic mixed arrays of characters and numbers.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Uzair Khan")
                                .email("uzair@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://bfhl-production-uzair.up.railway.app").description("Railway Production Environment (Example)")
                ));
    }
}
