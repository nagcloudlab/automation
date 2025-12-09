package com.upi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration for Transfer Service.
 * Provides interactive API documentation at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transferServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transfer Service API")
                        .description("""
                                REST API for Banking Operations - Fund Transfer Service
                                
                                This API provides endpoints for:
                                - **Account Management**: Create, read, update, and delete bank accounts
                                - **Fund Transfers**: Transfer funds between accounts with validation
                                - **Transaction History**: View complete audit trail of transactions
                                
                                Designed for REST Assured Testing Training.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Training Team")
                                .email("training@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
}
