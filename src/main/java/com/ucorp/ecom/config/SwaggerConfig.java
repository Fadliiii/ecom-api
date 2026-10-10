package com.ucorp.ecom.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
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

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT bearer token");

        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Spring boot ecommerce API")
                        .version("1.0")
                        .description("This is a spring boot project for ecommerce")
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .contact(new Contact()
                                .name("Fadlilatul Umam")
                                .email("fadlilatul.umam@gmail.com"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("This is a spring boot project for ecommerce")
                        .url("https://github.com/fadlilatul/ecommerce"))
                .components(
                        new Components()
                                .addSecuritySchemes("Bearer Authentication",bearerScheme))
                .addSecurityItem(bearerRequirement);
    }
}
