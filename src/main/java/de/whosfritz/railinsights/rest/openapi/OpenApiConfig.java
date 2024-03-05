package de.whosfritz.railinsights.rest.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        Server server = new Server();
        server.setUrl("https://railinsights.de");
        server.setDescription("Production Server");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("RailInsights API")
                        .description("API for RailInsights")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("RailInsights")
                                .url("https://github.com/whosFritz/Rail-Insights"))
                        .license(new License().name("License").url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
                );
    }
}