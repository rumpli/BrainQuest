package ch.quizinno.brainquest.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for generating and validating JWT tokens.
 */
// This annotation indicates that this class is a Spring managed bean.
@Component
public class JWTUtil {
    /**
     * Secret key used to sign JWT tokens.
     */
    private final String secretKey;
    /**
     * Expiration time for access tokens in milliseconds.
     */
    private final long ACCESS_TOKEN_EXPIRATION = 3600000; // 1 hour in milliseconds
    /**
     * Expiration time for refresh tokens in milliseconds.
     */
    private final long REFRESH_TOKEN_EXPIRATION = 86400000; // 1 day in milliseconds

    /**
     * Constructs a new JWTUtil and initializes the secret key.
     * All access tokens will be invalidated when the server is restarted, as the secret key is randomly generated each time.
     */
    public JWTUtil() {
        // Initialize a new KeyGenerator
        KeyGenerator keyGenerator;

        /*
            try
                - Create a new KeyGenerator instance for the HmacSHA256 algorithm
            catch NoSuchAlgorithmException
                - Throw a new RuntimeException with the message "Error initializing KeyGenerator" and the caught exception as the cause
         */
        try {
            // Create a new KeyGenerator instance for the HmacSHA256 algorithm
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            // Initialize the key generator with a key size of 256 bits
            keyGenerator.init(256);
            // Generate a secret key for signing JWTs
            this.secretKey = Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded()); // Generate a secret key for signing JWTs
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing KeyGenerator", e);
        }
    }

    /**
     * Generates a new access token for the given username.
     *
     * @param username the username to generate the token for
     * @return the generated access token
     */
    public String generateAccessToken(String username) {
        /*
            .builder() - Create a new JWT builder
            .setSubject(username) - Set the subject of the JWT to the given username
            .claim("type", "access") - Add a custom claim "type" with the value "access"
            .setIssuedAt(new Date()) - Set the issued at date to the current date
            .setExpiration() - Set the expiration date to the current date plus the access token expiration time
            .signWith() - Sign the JWT with the HmacSHA256 algorithm and the secret key
            .compact() - Compact the JWT into a string
         */
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Generates a new refresh token for the given username.
     *
     * @param username the username to generate the token for
     * @return the generated refresh token
     */
    public String generateRefreshToken(String username) {
        /*
            .builder() - Create a new JWT builder
            .setSubject(username) - Set the subject of the JWT to the given username
            .claim("type", "access") - Add a custom claim "type" with the value "access"
            .setIssuedAt(new Date()) - Set the issued at date to the current date
            .setExpiration() - Set the expiration date to the current date plus the access token expiration time
            .signWith() - Sign the JWT with the HmacSHA256 algorithm and the secret key
            .compact() - Compact the JWT into a string
         */
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Extracts the username from the given token.
     *
     * @param token the token to extract the username from
     * @return the extracted username
     */
    public String extractUsername(String token) {
        /*
            .parser() - Create a new JWT parser
            .setSigningKey(secretKey) - Set the signing key to the secret key
            .parseClaimsJws(token) - Parse the given token
            .getBody() - Get the body of the token
            .getSubject() - Get the subject of the token
         */
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the given token for the given username.
     *
     * @param token    the token to validate
     * @param username the username to validate the token for
     * @return true if the token is valid for the given username, false otherwise
     */
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (username.equals(extractedUsername) && !isTokenExpired(token));
    }

    /**
     * Checks if the given token is expired.
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        /*
            .parser() - Create a new JWT parser
            .setSigningKey(secretKey) - Set the signing key to the secret key
            .parseClaimsJws(token) - Parse the given token
            .getBody() - Get the body of the token
            .getExpiration() - Get the expiration date of the token
            .before(new Date()) - Check if the expiration date is before the current date
         */
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    /**
     * Checks if the given token is an access token.
     *
     * @param token the token to check
     * @return true if the token is an access token, false otherwise
     */
    public boolean isAccessToken(String token) {
        /*
            .parser() - Create a new JWT parser
            .setSigningKey(secretKey) - Set the signing key to the secret key
            .parseClaimsJws(token) - Parse the given token
            .getBody() - Get the body of the token
            .get("type", String.class) - Get the value of the "type" claim as a string
         */
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        // Get the token type from the "type" claim
        String tokenType = claims.get("type", String.class);

        return "access".equals(tokenType);
    }

    /**
     * Checks if the given token is a refresh token.
     *
     * @param token the token to check
     * @return true if the token is a refresh token, false otherwise
     */
    public boolean isRefreshToken(String token) {
        /*
            .parser() - Create a new JWT parser
            .setSigningKey(secretKey) - Set the signing key to the secret key
            .parseClaimsJws(token) - Parse the given token
            .getBody() - Get the body of the token
            .get("type", String.class) - Get the value of the "type" claim as a string
         */
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        // Get the token type from the "type" claim
        String tokenType = claims.get("type", String.class);

        return "refresh".equals(tokenType);
    }
}
