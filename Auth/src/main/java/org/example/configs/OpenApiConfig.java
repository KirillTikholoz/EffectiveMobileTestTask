package org.example.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API for the authorization service",
                description = "API for creating and updating jwt tokens"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}
