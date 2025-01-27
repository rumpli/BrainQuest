package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.LoginDTO;
import ch.quizinno.brainquest.dtos.RefreshTokenDTO;
import ch.quizinno.brainquest.entities.User;
import ch.quizinno.brainquest.repositories.UserRepository;
import ch.quizinno.brainquest.services.AnswerService;
import ch.quizinno.brainquest.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class to test the AuthController.
 */
// Specifies the controller to be tested.
@WebMvcTest(AuthController.class)
// Ignore the security configuration for the test.
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    /**
     * MockMvc for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private MockMvc mockMvc;
    /**
     * ObjectMapper for testing.
     */
    @Autowired
    // Injected required dependency into the bean.
    private ObjectMapper objectMapper;
    /**
     * MockBean for testing.
     */
    // Mock required dependency.
    @MockBean
    private AuthenticationManager authenticationManager;
    /**
     * MockBean for testing.
     */
    // Mock required dependency.
    @MockBean
    private JWTUtil jwtUtil;


    /**
     * Test login.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testLogin() throws Exception {
        // build loginDTO
        LoginDTO loginDTO = LoginDTO.builder()
                .username("test")
                .password("test")
                .build();
        // mock the authenticate method
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        // mock the generateAccessToken method
        when(jwtUtil.generateAccessToken(loginDTO.getUsername())).thenReturn("accessToken");
        // mock the generateRefreshToken method
        when(jwtUtil.generateRefreshToken(loginDTO.getUsername())).thenReturn("refreshToken");

        // perform post request
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    /**
     * Test login with wrong username.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testRefreshToken() throws Exception {
        // build refreshTokenDTO
        RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .refreshToken("refreshToken")
                .build();
        // mock the extractUsername method
        when(jwtUtil.extractUsername(refreshTokenDTO.getRefreshToken())).thenReturn("test");
        // mock the isRefreshToken method
        when(jwtUtil.isRefreshToken(refreshTokenDTO.getRefreshToken())).thenReturn(true);
        // mock the validateToken method
        when(jwtUtil.validateToken(refreshTokenDTO.getRefreshToken(), "test")).thenReturn(true);
        // mock the generateAccessToken method
        when(jwtUtil.generateAccessToken("test")).thenReturn("accessToken");

        // perform post request
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(refreshTokenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

}