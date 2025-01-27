package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.User;
import ch.quizinno.brainquest.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserService.
 */
// Create application context for testing
@SpringBootTest
// Single database transaction for all tests
@Transactional
// Create a new instance of the test class for each test method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Reset the context after each test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceTest {
    /**
     * UserService for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private UserService userService;
    /**
     * UserRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private UserRepository userRepository;
    /**
     * PasswordEncoder for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method to setup data for testing.
     */
    // Run before all tests in the class
    @BeforeAll
    public void setup() {
        // create user
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        userRepository.save(user);
    }

    /**
     * Test for loadUserByUsername method.
     */
    @Test
    public void testLoadUserByUsername() {
        // load user by username
        UserDetails user = userService.loadUserByUsername("test");
        // check if user is found
        assertEquals("test", user.getUsername());
    }

    /**
     * Test for loadUserByUsername method.
     */
    @Test
    public void testLoadUserByUsername_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername("unknown"));
    }

    /**
     * Test for getUserByUsername method.
     */
    @Test
    public void testGetUserByUsername() {
        // get user by username
        User user = userService.getUserByUsername("test").get();
        // check if user is found
        assertEquals("test", user.getUsername());
    }

    /**
     * Test for saveUser method.
     */
    @Test
    public void testSaveUser() {
        // create new user
        User user = new User();
        user.setUsername("test2");
        user.setPassword("test2");
        // save user
        userService.saveUser(user);
        // check if user is saved
        assertEquals("test2", user.getUsername());
        // password should be encrypted
        assertNotEquals("test2", user.getPassword());
    }
}
