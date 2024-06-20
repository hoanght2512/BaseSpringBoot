package com.hoanght.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                              .title("Sample API")
                              .version("1.0.0")
                              .description("This is a sample OpenAPI 3.1 specification")
                              .contact(new io.swagger.v3.oas.models.info.Contact()
                                               .email("contact@example.com")
                                               .name("John Doe")
                                               .url("https://example.com")));
    }
}
