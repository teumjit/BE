package com.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "틈짓 API",
        version = "1.0.0",
        description = """
            틈짓의 API 문서입니다.
            """
    )
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server()
                    .url("https://teumjit.up.railway.app")
                    .description("Railway Production Server")
            ));
    }
}