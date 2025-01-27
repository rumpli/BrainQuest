package ch.quizinno.brainquest.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAPI configuration class.
 */
// This annotation indicates that this class contains configuration methods.
@Configuration
public class OpenApiConfig {

    /**
     * Application URL.
     */
    // This annotation indicates that a field should be resolved by the environment variable.
    @Value("${APP_URL:http://localhost:8080}")
    private String appUrl;

    /**
     * Custom OpenAPI configuration.
     *
     * @return OpenAPI object
     */
    // This annotation indicates that a method produces a bean to be managed by the Spring container.
    @Bean
    public OpenAPI customOpenAPI() {
        // Create HTTP server
        Server httpServer = new Server();
        httpServer.setUrl(appUrl);

        // Add servers to OpenAPI
        List<Server> servers = new ArrayList<>();
        servers.add(httpServer);

        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("BrainQuest API")
                        .description("This is a api for the game BrainQuest.")
                )
                .servers(servers);
    }
}