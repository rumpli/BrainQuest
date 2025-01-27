package ch.quizinno.brainquest.filters;

import ch.quizinno.brainquest.services.UserService;
import ch.quizinno.brainquest.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom authentication filter to validate JWT tokens
 */
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT utility class
     */
    private final JWTUtil jwtUtil;
    /**
     * User service
     */
    private final UserService userService;

    /**
     * Constructor
     *
     * @param jwtUtil     JWT utility class
     * @param userService User service
     */
    public CustomAuthenticationFilter(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Filter method
     *
     * @param request     HTTP request
     * @param response    HTTP response
     * @param filterChain Filter chain
     * @throws ServletException if an error occurs
     * @throws IOException      if an error occurs
     */
    // This annotation overrides the parent class method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the token from the Authorization header
        String token = request.getHeader("Authorization");

        // Check if the token looks like a JWT
        if (token != null && token.startsWith("Bearer ")) {
            // Strip "Bearer " prefix
            token = token.substring(7);

            /*
                try
                    - Extract the username from the token
                    - Check if the user is not already authenticated
                    - Check if the token is an access token
                    - Load the user details
                    - Validate the token
                    - Authenticate the user
                catch
                    - Set the response status to 401
                    - Write an error message to the response
             */
            try {
                String username = jwtUtil.extractUsername(token);

                // Check if the user is not already authenticated
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Ensure it's an access token
                    if (jwtUtil.isAccessToken(token)) {
                        // Load the user details
                        UserDetails userDetails = userService.loadUserByUsername(username);

                        // Validate the token
                        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                            // Authenticate the user
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            // Set the authentication in the security context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } else {
                        throw new IllegalArgumentException("Token is not an access token");
                    }
                }
            } catch (Exception e) {
                // Set the response status to 401 and write an error message
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: " + e.getMessage());

                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
