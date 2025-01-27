package ch.quizinno.brainquest.configs;

import ch.quizinno.brainquest.filters.CustomAuthenticationFilter;
import ch.quizinno.brainquest.services.UserService;
import ch.quizinno.brainquest.utils.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration class.
 */
// This annotation indicates that this class contains configuration methods.
@Configuration
public class SecurityConfig {

    /**
     * JWT utility class.
     */
    private final JWTUtil jwtUtil;
    /**
     * Custom user details service.
     */
    private final UserService userDetailsService;

    /**
     * Constructor.
     *
     * @param jwtUtil            JWT utility class.
     * @param userDetailsService Custom user details service.
     */
    public SecurityConfig(JWTUtil jwtUtil, UserService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Security filter chain configuration.
     *
     * @param http HttpSecurity object.
     * @return SecurityFilterChain object.
     * @throws Exception if an error occurs.
     */
    // This annotation indicates that the return value of this method should be registered as a bean.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
            .cors - Configures CORS (Cross-Origin Resource Sharing) support.
            .csrf - Disables Cross-Site Request Forgery protection since we are using JWT tokens.
            .authorizeHttpRequests - Configures authorization rules.
            .addFilterBefore - Adds the JWT authentication filter before the UsernamePasswordAuthenticationFilter.
         */
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/topics").permitAll()
                        .requestMatchers("/api/topics/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/questions/quiz-question").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/questions/{id}/correct").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/questions/{id}/joker").permitAll()
                        .requestMatchers("/api/questions/**").authenticated()
                        .requestMatchers("/api/answers/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/highscores").permitAll()
                        .requestMatchers("/api/highscores/**").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        // Returns the built HttpSecurity object.
        return http.build();
    }

    /**
     * CORS configuration source.
     *
     * @return CORS configuration source.
     */
    private CorsConfigurationSource corsConfigurationSource() {
        // Fetch origins from environment variables
        String appUrl = System.getenv().getOrDefault("APP_URL", "http://localhost:8080");
        String frontendUrl = System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:5000");

        // Add multiple origins to the CORS configuration
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(appUrl, frontendUrl));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Password encoder.
     *
     * @return Password encoder.
     */
    // This annotation indicates that the return value of this method should be registered as a bean.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager.
     *
     * @param config Authentication configuration.
     * @return Authentication manager.
     * @throws Exception if an error occurs.
     */
    // This annotation indicates that the return value of this method should be registered as a bean.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
