package ch.quizinno.brainquest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application.
 */
// This annotation defines the OpenAPI definition.
@OpenAPIDefinition(
        info = @Info(title = "BrainQuest API", version = "1.0")
)
// This annotation defines a security scheme.
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
// This annotation indicates that this class is a Spring Boot application.
@SpringBootApplication
public class BrainQuestApplication {

    /**
     * Main method of the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BrainQuestApplication.class, args);
    }

}
