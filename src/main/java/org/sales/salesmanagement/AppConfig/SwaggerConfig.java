package org.sales.salesmanagement.AppConfig;



import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.version}")
    private String version;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Management")
                        .description("API documentation for SalesManagement Application")
                        .version(version));
    }

    @Bean
    public GroupedOpenApi authEndpoint() {
        return GroupedOpenApi.builder().group("Authentication Controller")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi forgotPasswordEndpoint() {
        return GroupedOpenApi.builder().group("Forgot Password Controller")
                .pathsToMatch("/api/v1/forgot-password/**")
                .build();
    }

    @Bean
    public GroupedOpenApi registrationEndpoint() {
        return GroupedOpenApi.builder().group("Registration Controller")
                .pathsToMatch("/api/v1/registration/**")
                .build();
    }

}
