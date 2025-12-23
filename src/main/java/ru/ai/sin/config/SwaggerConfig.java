package ru.ai.sin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ai.sin.property.SwaggerProperties;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion()))
                .servers(List.of(
                        new Server()
                                .url(swaggerProperties.getServers().get(0).getUrl())
                                .description(swaggerProperties.getServers().get(0).getDescription()),
                        new Server()
                                .url(swaggerProperties.getServers().get(1).getUrl())
                                .description(swaggerProperties.getServers().get(1).getDescription())
                ));
    }
}
