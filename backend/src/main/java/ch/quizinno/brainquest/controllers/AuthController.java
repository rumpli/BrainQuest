package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.LoginDTO;
import ch.quizinno.brainquest.dtos.RefreshTokenDTO;
import ch.quizinno.brainquest.dtos.TokenDTO;
import ch.quizinno.brainquest.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests
 */
// Spring annotation to indicate that this class is a REST controller.
@RestController
// Spring annotation to map HTTP requests to /api/auth.
@RequestMapping("/api/auth")
// Swagger annotation to describe the API endpoints for authentication.
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthController {

    /**
     * Service for authentication.
     */
    // Injected required dependency into the bean.
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * JWT Utility.
     */
    // Injected required dependency into the bean.
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Logs in a user with the specified username and password.
     *
     * @param loginData the login data
     * @return the access token and refresh token
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping("/login")
    // Swagger annotation to describe the API endpoint for user login.
    @Operation(summary = "User login", description = "Login with username and password")
    // Swagger annotation to describe the API response for user login.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = {
                            @Content(schema = @Schema(implementation = TokenDTO.class))
                    }),
    })
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginData) {
        // Authenticate the user with the specified username and password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.getUsername(),
                        loginData.getPassword()
                )
        );

        // Generate access token and refresh token.
        String username = authentication.getName();
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        return ResponseEntity.ok(new TokenDTO(accessToken, refreshToken));
    }

    /**
     * Refreshes the access token with the specified refresh token.
     *
     * @param tokenData the refresh token data
     * @return the new access token and refresh token
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping("/refresh")
    // Swagger annotation to describe the API endpoint for refreshing access token.
    @Operation(summary = "Refresh access token", description = "Refresh access token with refresh token")
    // Swagger annotation to describe the API response for refreshing access token.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed",
                    content = {
                            @Content(schema = @Schema(implementation = TokenDTO.class))
                    }),
    })
    public ResponseEntity<TokenDTO> refresh(@RequestBody RefreshTokenDTO tokenData) {
        String refreshToken = tokenData.getRefreshToken();

        // Check that refresh token is provided.
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Extract username from refresh token.
        String username = jwtUtil.extractUsername(refreshToken);

        // Ensure type refresh token and is valid.
        if (jwtUtil.isRefreshToken(refreshToken) && jwtUtil.validateToken(refreshToken, username)) {
            // Generate new access token.
            String accessToken = jwtUtil.generateAccessToken(username);
            return ResponseEntity.ok(new TokenDTO(accessToken, refreshToken));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
