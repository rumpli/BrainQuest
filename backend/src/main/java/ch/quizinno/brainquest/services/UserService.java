package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.User;
import ch.quizinno.brainquest.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing users.
 */
// Spring annotation to indicate that this class is a service.
@Service
public class UserService implements UserDetailsService {

    /**
     * Repository for managing users.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified UserRepository.
     *
     * @param userRepository the repository to manage users
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves user details by its username for authentication.
     *
     * @param username the username of the user to retrieve
     * @return the user with the specified username
     * @throws UsernameNotFoundException if the user is not found
     */
    // This annotation indicates that this method overrides a method in the UserDetailsService interface.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Use full name of the class to avoid ambiguity with User Entity
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    /**
     * Retrieves a user by its username.
     *
     * @param username the username of the user to retrieve
     * @return the user with the specified username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a user.
     *
     * @param user the user to save
     */
    public void saveUser(User user) {
        // Use BCryptPasswordEncoder to encode the password before saving it
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);
    }
}
