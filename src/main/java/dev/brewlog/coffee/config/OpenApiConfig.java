package dev.brewlog.coffee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coffeeApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coffee API")
                        .description("API para registrar recetas de café de especialidad, "
                                + "llevar una bitácora de preparaciones y ver su evolución en el tiempo.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Marlon Vera")));
    }
}