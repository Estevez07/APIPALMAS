package com.laspalmas.api.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.parser.OpenAPIV3Parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

      @Bean
    public OpenAPI customOpenAPI() {
        // Lee el archivo YAML desde resources
        return new OpenAPIV3Parser().read("src/main/resources/openapi/openapi-custom.yaml");
    }

}