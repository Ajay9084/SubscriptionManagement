package com.example.purchase_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI purchaseServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Purchase Service API")
                        .description("Handles product catalog management and purchase processing. " +
                                "On successful purchase, automatically registers a subscription via the Subscription Service.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Platform Team")
                                .email("platform@example.com")));
    }
}
