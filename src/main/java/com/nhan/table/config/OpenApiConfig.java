package com.nhan.table.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
@OpenAPIDefinition(
        info = @Info(
                contact =@Contact(
                        name = "Nhan",
                        email = "huunhantran10@gmail.com",
                        url = "https://nhanisme.click"
                ),
                description = "OpenApi documentation for Spring table service",
                title = "OpenApi specification -Nhan",
                version = "1.0"
        ),
        servers = {
                @Server (
                        description = "local env",
                        url = "http://localhost:8080"
                ),
                @Server (
                        description = "prod env",
                        url = "http://localhost:8081"
                )
        }
)
public class OpenApiConfig {
}
