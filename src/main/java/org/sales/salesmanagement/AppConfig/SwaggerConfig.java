package org.sales.salesmanagement.AppConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Integrated Software Data Services", description = "API documentation for Customer Application", version = "${swagger.version}"))
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SwaggerConfig {
    @Value("${swagger.version}")
    private String version;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Integrated Software Data Services")
                        .description("API documentation for Customer Application")
                        .version(version));
    }
    @Bean
    public GroupedOpenApi groupedOpenApi()  {
        return GroupedOpenApi.builder()
                .group("customer-api")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
