package com.biznopay.v1.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${SERVER.URL}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BiznoPay API")
                        .description("Sistema de Pagamentos Resiliente — Moçambique")
                        .version("1.0.0"))
                .servers(List.of(new Server().url(serverUrl).description("Server")));
    }
}