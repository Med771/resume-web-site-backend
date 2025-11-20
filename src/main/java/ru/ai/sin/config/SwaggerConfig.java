package ru.ai.sin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.version}")
    private String version;

    @Value("${swagger.servers[0].url}")
    private String localUrl;

    @Value("${swagger.servers[0].description}")
    private String localDesc;

    @Value("${swagger.servers[1].url}")
    private String prodUrl;

    @Value("${swagger.servers[1].description}")
    private String prodDesc;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version))
                .servers(List.of(
                        new Server().url(localUrl).description(localDesc),
                        new Server().url(prodUrl).description(prodDesc)
                ));
    }
}
